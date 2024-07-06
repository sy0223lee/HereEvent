// 이벤트 리스트와 지도 마커 출력
function printMapList(map, markers, markerImg){
    let location;
    let stateList = [];
    let typeList = [];

    $('input[name=map]:checked').each(function () {
        location = $(this).val();
    });
    $('input[name=state]:checked').each(function () {
        stateList.push($(this).val());
    });
    $('input[name=type]:checked').each(function () {
        typeList.push($(this).val());
    });

    console.log(location);
    console.log(stateList);
    console.log(typeList);

    let data = {'location': location, 'state': stateList, 'type': typeList};
    console.log(data);

    $.ajax({
        url: '/hereevent/map/list',
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            // 기존 마커 제거
            for (let i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
            }
            markers.length = 0;

            // 카드 리스트 html 문자열
            let printData = "";

            result.forEach(event => {
                /* 문자열이 <,> 로 싸여 있는 경우 태그로 인식하므로 정규식으로 대체 */
                event.name = event.name.replace(/</g,"&lt;");
                event.name = event.name.replace(/>/g,"&gt;");

                /* 카드 리스트 생성 */
                printData += "<div class='card'>";

                if(event.img_path == null){
                    printData += "<img src='/hereevent/images/default_img.png' class='card-img-top' alt='default_img'>";
                }else {
                    printData += "<img src='/hereevent/download/event/" + event.img_path + "' class='card-img-top' alt='" + event.img_path + "'>";
                }
                printData += "<div class='card-body'>" +
                    "             <h5 class='card-title'>" + event.name + "</h5>";

                /* 예약 방식 */
                if(event.type === "reserve"){
                    printData += "<div class='event-type'><span>사전에약</span></div>";
                }else if((event.type === "wait")){
                    printData += "<div class='event-type'><span>현장대기</span></div>";
                }else{
                    printData += "<div class='event-type'><span>사전에약</span><span>현장대기</span></div>";
                }

                printData += "           <div class='card-text'><span>" + event.start_date + "</span> ~ <span>" + event.end_date + "</span></div>" +
                    "                    <div class='card-text'>" + event.addr + "</div>" +
                    "                    <div class='card-btn'><a href='/hereevent/event/" + event.event_no + "' class='btn-primary'>상세보기</a></div>" +
                    "                </div>" +
                    "            </div>";

                /* 지도 마커 생성 */
                // 주소-좌표 변환 객체를 생성합니다
                let geocoder = new kakao.maps.services.Geocoder();

                // 주소로 좌표를 검색합니다
                geocoder.addressSearch(event.addr, function (result, status) {

                    // 정상적으로 검색이 완료됐으면
                    if (status === kakao.maps.services.Status.OK) {

                        let coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                        // 결과값으로 받은 위치를 마커로 표시합니다
                        let marker = new kakao.maps.Marker({
                            map: map,
                            position: coords,
                            title: event.name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                            image: markerImg
                        });

                        markers.push(marker); // 마커 배열에 마커 추가

                        // 커스텀 오버레이에 표시할 컨텐츠 입니다
                        // 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
                        // 별도의 이벤트 메소드를 제공하지 않습니다
                        let content = '<div class="wrap">' +
                            '    <div class="info">' +
                            '        <div class="title">' +
                            event.name +
                            '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
                            '        </div>' +
                            '        <div class="body">' +
                            '            <div class="img">' +
                            '                <img src="/hereevent/download/event/' + event.img_path + '" width="73" height="70">' +
                            '           </div>' +
                            '            <div class="desc">' +
                            '                <div class="ellipsis">' + event.addr + '</div>' +
                            '                <div><a href="/hereevent/event/' + event.event_no + '" class="link"> 상세보기 </a></div>' +
                            '                <div>' + event.type + '</div>' +
                            '            </div>' +
                            '        </div>' +
                            '    </div>' +
                            '</div>';

                        // 마커 위에 커스텀오버레이를 표시합니다
                        // 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS 를 이용해 위치를 설정했습니다
                        let overlay = new kakao.maps.CustomOverlay({
                            content: content,
                            position: marker.getPosition()
                        });

                        // 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
                        function closeOverlay() {
                            overlay.setMap(null);
                        }

                        // 닫기 버튼에 클릭 이벤트를 추가합니다
                        $(document).on('click', '.close', closeOverlay);

                        // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
                        kakao.maps.event.addListener(marker, 'click', function () {
                            overlay.setMap(map);
                        });
                    }
                });
            });
            $("#list-box").empty(); // <tr>태그 모두 지우기
            $("#list-box").append(printData);
            window.scrollTo(0, 0);
        },
        error: function (obj, msg) {
            console.log("요청 실패: " + obj + ", " + msg);
        }
    });
}