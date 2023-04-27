<%@ page contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LIVE 테스트 페이지</title>
    <script type="text/javascript">
        var g_webSocket = null;
        var id = null;
        window.onload = function () {

            /**
             * 웹소켓 사용자 연결 성립하는 경우 호출
             */
            if (g_webSocket != null ) {
                // g_webSocket.onopen = function (message) {
                //     const userId = document.getElementById("userId")
                //     addLineToChatBox(" status Server is connected.");
                // };
                //
                //
                // /**
                //  * 웹소켓 메시지(From Server) 수신하는 경우 호출
                //  */
                // g_webSocket.onmessage = function (message) {
                //     addLineToChatBox(message.data);
                // };
                //
                // /**
                //  * 웹소켓 사용자 연결 해제하는 경우 호출
                //  */
                // g_webSocket.onclose = function (message) {
                //     addLineToChatBox("Server is disconnected.");
                // };
                //
                // /**
                //  * 웹소켓 에러 발생하는 경우 호출
                //  */
                // g_webSocket.onerror = function (message) {
                //     addLineToChatBox("Error!");
                // };
            }
        }

        function connection() {
            g_webSocket = new WebSocket("ws://localhost:8081/ws/live/" + "seoul" + "/" + document.getElementById("userId").value);
            g_webSocket.onopen = function (message) {
                const userId = document.getElementById("userId")
                addLineToChatBox(" status Server is connected.");
            };

            /**
             * 웹소켓 메시지(From Server) 수신하는 경우 호출
             */
            g_webSocket.onmessage = function (message) {
                addLineToChatBox(message.data);
            };

            /**
             * 웹소켓 사용자 연결 해제하는 경우 호출
             */
            g_webSocket.onclose = function (message) {
                addLineToChatBox("Server is disconnected.");
            };

            /**
             * 웹소켓 에러 발생하는 경우 호출
             */
            g_webSocket.onerror = function (message) {
                addLineToChatBox("Error!");
            };
        }

        /**
         * 채팅 박스영역에 내용 한 줄 추가
         */
        function addLineToChatBox(_line) {
            if (_line == null) {
                _line = "";
            }

            var chatBoxArea = document.getElementById("chatBoxArea");
            chatBoxArea.value += _line + "\n";
            chatBoxArea.scrollTop = chatBoxArea.scrollHeight;
        }

        /**
         * Send 버튼 클릭하는 경우 호출 (서버로 메시지 전송)
         */
        function sendButton_onclick() {
            var inputMsgBox = document.getElementById("inputMsgBox");
            var userId = document.getElementById("userId")
            var targetId = document.getElementById("targetId")
            // if (inputMsgBox == null || inputMsgBox.value == null || inputMsgBox.value.length == 0) {
            //     return false;
            // }

            // var chatBoxArea = document.getElementById("chatBoxArea");
            //
            // if (g_webSocket == null || g_webSocket.readyState == 3) {
            //     chatBoxArea.value += "Server is disconnected.\n";
            //     return false;
            // }
            // 서버로 메시지 전송
            g_webSocket.send(
                "{\"city\": \"seoul\"," +
                "\"memberUuid\":\"" + userId.value + "\"," +
                "\"latitude\":" + "\"23\"," +
                "\"longitude\":" + "\"123\"," +
                "\"nickname\":\"" + userId.value + "\"," +
                "\"gender\":\"" + userId.value + "\"," +
                "\"age\":" + "\"20대\"" +
                "}"
            );
            inputMsgBox.value = "";
            inputMsgBox.focus();

            return true;
        }

        /**
         * Disconnect 버튼 클릭하는 경우 호출
         */
        function disconnectButton_onclick() {
            if (g_webSocket != null) {
                const userId = document.getElementById("userId")
                g_webSocket.close();
            }
        }


        /**
         * inputMsgBox 키입력하는 경우 호출
         */
        function inputMsgBox_onkeypress() {
            if (event == null) {
                return false;
            }

            // 엔터키 누를 경우 서버로 메시지 전송
            var keyCode = event.keyCode || event.which;
            if (keyCode == 13) {
                sendButton_onclick();
            }
        }

    </script>
</head>
<body>
<input id="userId" style="width: 250px;" type="text">
<input id="targetId" style="width: 250px;" type="text">
<br/>
<input id="inputMsgBox" style="width: 250px;" type="text" onkeypress="inputMsgBox_onkeypress()">
<input id="sendButton" value="Send" type="button" onclick="sendButton_onclick()">
<input id="disconnectButton" value="Disconnect" type="button" onclick="disconnectButton_onclick()">
<br/>
<textarea id="chatBoxArea" style="width: 100%;" rows="10" cols="50" readonly="readonly"></textarea>
<button id="connection" onclick="connection()">connection</button>
</body>
</html>