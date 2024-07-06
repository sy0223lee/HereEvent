function printCategoryList(categoryNo){
    let stateList = [];
    let typeList = [];

    $('input[name=state]:checked').each(function () {
        stateList.push($(this).val());
    });
    $('input[name=type]:checked').each(function () {
        typeList.push($(this).val());
    });

    console.log(stateList);
    console.log(typeList);

    let data = {'state': stateList, 'type': typeList};
    console.log(data);

    $.ajax({
        url: '/hereevent/list/category/' + categoryNo,
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            let printData = "";
            result.forEach(event => {
                event.name = event.name.replace(/</g,"&lt;");
                event.name = event.name.replace(/>/g,"&gt;");

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

function printEventList(tag){
    let stateList = [];
    let typeList = [];

    $('input[name=state]:checked').each(function () {
        stateList.push($(this).val());
    });
    $('input[name=type]:checked').each(function () {
        typeList.push($(this).val());
    });

    console.log(tag);
    console.log(stateList);
    console.log(typeList);

    let data = {'tag': tag, 'state': stateList, 'type': typeList};
    console.log(data);

    $.ajax({
        url: '/hereevent/list',
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            let printData = "";
            result.forEach((event, index) => {
                event.name = event.name.replace(/</g,"&lt;");
                event.name = event.name.replace(/>/g,"&gt;");

                printData += "<div class='card'>";

                if(event.img_path == null){
                    printData += "<img src='/hereevent/images/default_img.png' class='card-img-top' alt='default_img'>";
                }else {
                    printData += "<img src='/hereevent/download/event/" + event.img_path + "' class='card-img-top' alt='" + event.img_path + "'>";
                }

                if(tag === "star" || tag === "popular"){
                    printData += "<h2 class='event-rank'>" + (index+1) + "</h2>"
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
