<!doctype html>
<html lang="en" xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-12">
            <h3>채팅방 리스트</h3>
        </div>
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="setToken">로그인</button>
        </div>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">방제목</label>
        </div>
        <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId" v-on:click="enterRoom(item.roomId)">
            {{item.name}}
        </li>
    </ul>
</div>
<!-- JavaScript -->
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    var reconnect = 0;

    var tokenString = 'wschat.access_token';
    localStorage.removeItem(tokenString);
    function setToken() {
        localStorage.setItem(tokenString,prompt('token을 입력해 주세요.'));
        axios.get('/user/me',
            {
                headers: {
                    Authorization: 'Bearer ' + getToken() //the token is a variable which holds the token
                }
            }).then(response => {
            localStorage.setItem('user',response.data.id);
        });
    };
    function getToken() {
        return localStorage.getItem(tokenString);
    }
    function getUser() {
        return localStorage.getItem('user');
    }
    if(getToken()==null){
        setToken();
    }else{
        alert(getToken());
    }

    var vm = new Vue({
        el: '#app',
        data: {
            room_name : '',
            chatrooms: [
            ]
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function() {
                axios.get('/chat/rooms',
                    {
                        headers: {
                            Authorization: 'Bearer ' + getToken() //the token is a variable which holds the token
                        }
                    }).then(response => { this.chatrooms = response.data; });
            },
            createRoom: function() {
                if("" === this.room_name) {
                    alert("방 제목을 입력해 주십시요.");
                    return;
                } else {
                    var params = new URLSearchParams();
                    params.append("name",this.room_name);
                    //users추가해야댐.

                    axios.post('/chat/room', params,
                        {
                            headers: {
                                Authorization: 'Bearer ' + getToken() //the token is a variable which holds the token
                            }
                        })
                        .then(
                            response => {
                                alert(response.data.name+"방 개설에 성공하였습니다.")
                                this.room_name = '';
                                this.findAllRoom();
                            }
                        )
                        .catch( response => { alert("채팅방 개설에 실패하였습니다."); } );
                }
            },
            enterRoom: function(roomId) {

                    localStorage.setItem('wschat.roomId',roomId);
                    location.href="/chat/enter/room/"+roomId;

            },
            recvMessage: function(recv) {
                //this.messages.unshift({"type":recv.type,"sender":recv.type=='ENTER'?'[알림]':recv.sender,"message":recv.message})
                console.log(recv);
            }
        }
    });


    function connect() {
        var keyword = 'Authorization';

        // pub/sub event
        ws.connect({'Authorization': 'Bearer '+getToken()}, function(frame) {
            ws.subscribe("/sub/chat/users/"+getUser(), function(message) {
                console.log("이거 뭔데4");
                var recv = JSON.parse(message.body);
                vm.recvMessage(recv);
            },{
                'Authorization': 'Bearer ' + getToken() //the token is a variable which holds the token
            });
            ws.send("/pub/chat/message", {'Authorization': 'Bearer '+getToken()}, JSON.stringify({
                chatRoomId:1,
                message: "메시지"
            }));
        }, function(error) {
            console.log("이거 뭔데5");
            if(reconnect++ <= 5) {
                setTimeout(function() {
                    console.log("connection reconnect");
                    sock = new SockJS("/ws-stomp");
                    ws = Stomp.over(sock);
                    connect();
                },10*1000);
            }
        }
        );
    }
    connect();
</script>
</body>
</html>