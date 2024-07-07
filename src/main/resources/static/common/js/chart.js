/*날짜별 시작/종료 이벤트 수*/
function drawStartEndEvent(eventList) {
    let data = new google.visualization.DataTable();
    data.addColumn('string', '날짜');
    data.addColumn('number', '시작');
    data.addColumn('number', '종료');

    for (let event of eventList) {
        data.addRows([
            [event.date, event.start_cnt, event.end_cnt]
        ]);
    }

    let options = {
        title: '날짜별 시작/종료 이벤트 수',
        colors: ['#9575cd', '#33ac71'],
        height: 350,
    };

    let chart = new google.visualization.ColumnChart(document.getElementById('start_end_chart'));
    chart.draw(data, options);
}

/*이벤트 카테고리 비율*/
function drawCategoryRate(categoryList) {
    let data = new google.visualization.DataTable();
    data.addColumn('string', '카테고리');
    data.addColumn('number', '개수');

    for (let category of categoryList) {
        data.addRows([
            [category.name, category.cnt]
        ]);
    }

    let options = {
        title: '이벤트 카테고리 비율',
        pieHole: 0.4,
        height: 350,
    };

    let chart = new google.visualization.PieChart(document.getElementById('category_rate_chart'));
    chart.draw(data, options);
}

/*날짜별 신규 회원 가입 수*/
function drawNewMember(memberList) {
    let data = new google.visualization.DataTable();
    data.addColumn('string', '날짜');
    data.addColumn('number', '회원');

    for(let member of memberList) {
        data.addRows([
            [member.date, member.cnt]
        ]);
    }

    let options = {
        title: '날짜별 신규 회원 가입 수',
        height: 350,
        pointSize: 5,
    };

    let chart = new google.visualization.LineChart(document.getElementById('new_member_chart'));

    chart.draw(data, options);
}

/*예약/대기 상위 이벤트*/
function drawTopEvent(reserveList, waitList) {
    if(reserveList.length < 5){
        for(let i=reserveList.length; i<5; i++){
            reserveList[i] = {cnt: 0, name: null};
        }
    }
    if(waitList.length < 5){
        for(let i=waitList.length; i<5; i++){
            waitList[i] = {cnt: 0, name: null};
        }
    }

    let data = new google.visualization.DataTable();
    data.addColumn('string', '순위');
    data.addColumn('number', '예약');
    data.addColumn('number', '대기');

    for(let i=0; i<5; i++) {
        data.addRows([
            [(i+1)+"등", {v: reserveList[i].cnt, f: reserveList[i].name}, {v: waitList[i].cnt, f: waitList[i].name}]
        ]);
    }

    let options = {
        title: '예약/대기 상위 이벤트',
        chartArea: {width: '50%'},
        height: 350,
    };

    let chart = new google.visualization.BarChart(document.getElementById('top_event_chart'));
    chart.draw(data, options);
}