// 이벤트 리스트와 지도 마커 출력
function printList(map, markers, markerImg){
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
            let printData = "";
            result.forEach(event => {
                printData = printData
                    + '<div class="card" style="width: 18rem;">'
                    + '  <img src="/hereevent/download/event/' + event.img_path + '"class="card-img-top">'
                    + '  <div class="card-body">'
                    + '     <h5 class="card-title">' + event.name + '</h5>'
                    + '     <p class="card-text">' + event.addr + '</p>'
                    + '     <a href="/hereevent/event/' + event.event_no + '" class="btn btn-primary">상세페이지</a>'
                    + '  </div>'
                    + '</div>'
            });
            $("#list-box").empty(); // <tr>태그 모두 지우기
            $("#list-box").append(printData);

            // 기존 마커 제거
            for (let i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
            }
            markers.length = 0;

            result.forEach(event => {
                <!--주소로 검색해서 마커-->

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
                            '                <div><a href="/hereevent/event/' + event.event_no + '" class="link"> 상세페이지 </a></div>' +
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
            })//end foreach
        },
        error: function (obj, msg) {
            console.log("요청 실패: " + obj + ", " + msg);
        }
    });
}