// 체크박스 모두 선택, 선택 해제
function checkSelectAll()  {
    // 전체 체크박스
    const checkboxes = document.querySelectorAll('input[name="select"]');
    // 선택된 체크박스
    const checked = document.querySelectorAll('input[name="select"]:checked');
    // select all 체크박스
    const selectAll = document.querySelector('input[name="select-all"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}
function selectAll(selectAll)  {
    const checkboxes = document.getElementsByName('select');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    });
}

// 페이지네이션 버튼(전체 페이지 수, 현재 페이지 번호, 전체 조회된 요소의 개수)
function pageBtn(totalPages, pageNumber, totalElements) {
    // 보여줄 리스트가 없으면 페이지 버튼 출력 X
    if (totalElements === 0) {
        document.querySelector(".pagination").innerHTML = "";
        return false;
    }

    let pageBlock = 3; // 페이지 번호 5개씩 보여주기
    let blockNo = toInt(pageNumber / pageBlock) + 1; // 5개씩 보여줄 페이지 블럭 번호 계산
    let startPageNumber = (blockNo - 1) * pageBlock; // 페이지 시작 번호
    let endPageNumber = blockNo * pageBlock - 1; // 페이지 종료 번호

    // 마지막 페이지 버튼 번호가 전체 페이지 수보다 작은 경우
    if (endPageNumber > totalPages - 1) {
        endPageNumber = totalPages - 1;
    }

    let prevBlockPageNumber = (blockNo - 1) * pageBlock - 1; // 이전 페이지 블럭
    let nextBlockPageNumber = blockNo * pageBlock; // 다음 페이지 블럭

    let strHTML = "";

    // <, << 활성화/비활성화 처리
    if (prevBlockPageNumber >= 0) {
        // <, << 활성화
        strHTML += "<li class='page-item'><button onclick='movePage(0)' class='page-btn'><i class='bi bi-chevron-double-left'></i></button></li>";
        strHTML += "<li class='page-item'><button onclick='movePage(" + prevBlockPageNumber + ")' class='page-btn'><i class='bi bi-chevron-left'></i></button></li>";
    } else {
        // <, << 비활성화
        strHTML += "<li class='page-item disabled'><button class='page-btn'><i class='bi bi-chevron-double-left'></button></i></li>";
        strHTML += "<li class='page-item disabled'><button class='page-btn'><i class='bi bi-chevron-left'></i></button></li>";
    }

    // 페이징 번호 생성
    for (let i = startPageNumber; i <= endPageNumber; i++) {
        if (i === pageNumber) {
            strHTML += "<li class='page-item active'><button class='page-btn'>" + (i+1) + "</button></li>";
        } else {
            strHTML += "<li class='page-item'><button onclick='movePage(" + i + ")' class='page-btn'>" + (i+1) + "</button></li>";
        }
    }

    // >, >> 활성화/비활성화 처리
    if (nextBlockPageNumber < totalPages) {
        // >, >> 활성화
        strHTML += "<li class='page-item'><button onclick='movePage(" + nextBlockPageNumber + ")' class='page-btn'><i class='bi bi-chevron-right'></i></button></li>";
        strHTML += "<li class='page-item'><button onclick='movePage(" + (totalPages-1) + ")' class='page-btn'><i class='bi bi-chevron-double-right'></i></button></li>";
    } else {
        // >, >> 비활성화
        strHTML += "<li class='page-item disabled'><button class='page-btn'><i class='bi bi-chevron-right'></i></button></li>";
        strHTML += "<li class='page-item disabled'><button class='page-btn'><i class='bi bi-chevron-double-right'></i></button></li>";
    }

    $(".pagination").append(strHTML);
}

// 페이지 블럭 번호 int 변환
function toInt(value){
    if(value != null){
        return parseInt(value, 10);
    }
}

// 페이지 이동 버튼 - 전달 받은 페이지 번호로 이동
function movePage(pageNumber){
    // 컨트롤러에게 전해줄 파라미터
    let type = $("select[name='type']").val();
    let keyword = $('input[name="keyword"]').val();

    // 경로 이동
    location.href = "/hereevent/admin/review?type=" + type + "&keyword=" + keyword + "&page=" + pageNumber;
}