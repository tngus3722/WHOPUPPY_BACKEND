package com.whopuppy.serviceImpl;

import com.whopuppy.domain.criteria.AnimalListCriteria;
import com.whopuppy.mapper.AnimalMapper;
import com.whopuppy.service.AnimalService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whopuppy.domain.AnimalDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private AnimalMapper animalMapper;

    public static int INDENT_FACTOR = 4;

    final RestTemplate restTemplate;

    public AnimalServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${animal.service-key}")
    private String serviceKey;

    @Value("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?bgnde=")
    private String apiURL;

    @Override
    public void insertAnimalJson(List animalDTOList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("animalList", animalDTOList);
        animalMapper.insertAnimal(map);
    }

    public ResponseEntity insertAnimalList() throws IOException, ParseException, URISyntaxException {
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        cal.add(Calendar.DATE, -1);
        String startDate = date.format(cal.getTime());
        String endDate = date.format(currentDate);

        Integer pageNo = 0;
        Integer Rows = 100;
        Integer currentCount = 0;
        Integer totalCount = 0;

        do{
            String resultURL = apiURL + startDate + "&endde=" + endDate + "&pageNo=" + ++pageNo + "&numOfRows=" + Rows + "&";
            StringBuilder urlBuilder = new StringBuilder(resultURL);
            urlBuilder.append(URLEncoder.encode("ServiceKey", "UTF-8") + serviceKey);
            URI uri = new URI(urlBuilder.toString());

            String jsonStr = restTemplate.getForObject(uri, String.class);

            List<AnimalDTO> animalDTOList = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonStr);
            JSONObject parse_response = (JSONObject) jsonObj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray parse_array = (JSONArray) parse_items.get("item");

            JSONObject obj;
            for(int i = 0; i < parse_array.size(); i++) {
                obj = (JSONObject) parse_array.get(i);
                AnimalDTO animalDTO = new AnimalDTO(obj);
                animalDTOList.add(animalDTO);
                this.insertAnimalJson(animalDTOList);
            }

            currentCount = pageNo * Rows;
            String count = parse_body.get("totalCount").toString();
            totalCount = Integer.parseInt(count);

        }while(currentCount < totalCount);

        return new ResponseEntity(("리스트 입력이 완료되었습니다"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity searchAnimal(AnimalListCriteria animalListCriteria) throws Exception {
        List<AnimalDTO> animalDTOList;
        Long animalListTotalCount = animalMapper.searchAnimalTotalCount(animalListCriteria.getNoticeNo());
        if (animalListTotalCount > 0) {
            animalDTOList = animalMapper.searchAnimal(animalListCriteria);
        }
        else
            throw new Exception();

        return new ResponseEntity(animalDTOList, HttpStatus.OK);
    }

}
