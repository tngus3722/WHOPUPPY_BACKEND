package com.whopuppy.serviceImpl;

import com.whopuppy.domain.AnimalDTO;
import com.whopuppy.domain.criteria.AnimalListCriteria;
import com.whopuppy.mapper.AnimalMapper;
import com.whopuppy.service.AnimalService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class AnimalServiceImpl implements AnimalService {

    @Resource
    private AnimalMapper animalMapper;
    @Resource
    private RestTemplate restTemplate;

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

        do {
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
            
            for (int i = 0; i < parse_array.size(); i++)
                animalDTOList.add(new AnimalDTO((JSONObject) parse_array.get(i)));

            this.insertAnimalJson(animalDTOList);
            currentCount = pageNo * Rows;
            String count = parse_body.get("totalCount").toString();
            totalCount = Integer.parseInt(count);
        } while (currentCount < totalCount);

        return new ResponseEntity(("리스트 입력이 완료되었습니다"), HttpStatus.OK);
    }

    @Override
    public List<AnimalDTO> searchAnimalList(AnimalListCriteria animalListCriteria) {
        return animalMapper.searchAnimal(animalListCriteria);
    }

    @Override
    public AnimalDTO searchAnimal(Long idx){
        return animalMapper.selectAnimal(idx);
    }
}
