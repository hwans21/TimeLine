$('#showtimeLineBtn').on('click', function(e){
    console.log('timeline');
    $('.modal-title').text('타임라인 등록');
    $('.modal-body').html('<input type="text" id="timelineTxt" name="timeline" class="form-control" placeholder="제목입력 후 Enter키 입력">');
    $('.modal-footer').html(`<table class="table table-hover">
                              <thead class="table-dark">
                                <th class="g-col-xs-1" scope="col">시간</th>
                                <th class="g-col-xs-10" scope="col">제목</th>
                              </thead>
                              <tbody id="tempTable">


                              </tbody>
                            </table>`);
    $('#currentTime').val(currentTime)
});


$(document).on('keydown','#timelineTxt',function(e){
    if(e.keyCode === 13){
        insertTimeline();
    }
});

function insertTimeline(){
    $('#actionForm').attr('onsubmit', 'return false');
    var title = $('#timelineTxt').val();
    var id = timeline_insert(title);
    var data = timeline_oneselect(id);
    $('#tempTable').append(`
        <tr>
            <td style="display:none">`+data.id+`</td>
            <td>`+data.date+`</td>
            <td>`+data.title+`</td>
        </tr>
    `);
}

$(document).ready(function(e){
    var tagName = $('#timelineTable').prop('tagName')
    if(tagName === 'TBODY'){
        $('#monthBtn').trigger("click");
    }
});

var order = 'time';
var currentPage = 1;
$('#orderBtn').click(function(){
    if($('#orderBtn').val() === '시간순'){
        $('#orderBtn').val('조회순')
        order = 'count'

    } else{
        $('#orderBtn').val('시간순')
        order = 'time'
    }
    if($('#startDate').val() !== ''){
        if($('#endDate').val() !== ''){
            loadTable(1);
            currentPage = 1;
        }
    }
});

$(document).on('click','#todayBtn',function(e){
    var today = new Date();
    $('#startDate').val(dateToStr(today));
    $('#endDate').val(dateToStr(today));
    loadTable(1);
    currentPage = 1;
});

$(document).on('click','#weekBtn',function(e){
    var today = new Date();
    var lastweek = lastWeek();
    $('#startDate').val(dateToStr(lastweek));
    $('#endDate').val(dateToStr(today));
    loadTable(1);
    currentPage = 1;
});

$(document).on('click','#monthBtn',function(e){
    var today = new Date();
    var lastmonth = lastMonth();
    $('#startDate').val(dateToStr(lastmonth));
    $('#endDate').val(dateToStr(today));
    loadTable(1);
    currentPage = 1;
});

$('#startDate').change(function(e){
    if($('#startDate').val() !== ''){
        if($('#endDate').val() !== ''){
            loadTable(1);
            currentPage = 1;
        }
    }
});
$('#endDate').change(function(e){
    if($('#startDate').val() !== ''){
        if($('#endDate').val() !== ''){
            loadTable(1);
            currentPage = 1;
        }
    }
});


$(document).on('click','#prevBtn', function(e){
    currentPage = currentPage - 1;
    loadTable(currentPage);
});

$(document).on('click','#nextBtn', function(e){
    currentPage = currentPage + 1;
    loadTable(currentPage);
});

function loadTable(pageNum){
    var list = timeline_getList($('#startDate').val(),$('#endDate').val(),order, pageNum)
    var list2 = timeline_getList($('#startDate').val(),$('#endDate').val(),order, pageNum+1)
    if(pageNum != 1){
        $('#timelinepagenation').html('<li id="prevBtn" class="page-item"><a class="page-link" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>')
    }
    if(list2.length != 0){
        $('#timelinepagenation').html('<li id="nextBtn" class="page-item"><a class="page-link" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>')
    }
    var tabledata = "";
    $.each(list, function(i){
        tabledata += `<tr onClick="moveUrl('`+list[i].timelineId+`','`+list[i].youtubeId.youtubeUrl+`?t=`+list[i].timelineSec+`')"><td>`+list[i].timeFormat+`</td><td>`+list[i].timelineCount+
                    `</td><td>`+list[i].timelineTitle+`</td></tr>`
    });
    $('#timelineTable').html(tabledata);
}

function moveUrl(id, url){
    countup(id , url);
}

function currentTime(){
    var today = new Date();
    var year = today.getFullYear().toString().slice(-2);
    var month = ('0' + (today.getMonth() + 1)).slice(-2);
    var day = ('0' + today.getDate()).slice(-2);
    var hours = ('0' + today.getHours()).slice(-2);
    var minutes = ('0' + today.getMinutes()).slice(-2);
    var seconds = ('0' + today.getSeconds()).slice(-2);
    var datetimeString = year+month+day+'_'+hours+minutes+seconds;
    return datetimeString;
}

function dateToStr(date){
    var yyyy = date.getFullYear();
    var mm = (date.getMonth()+1) > 9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1);
    var dd = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
    return yyyy+"-"+mm+"-"+dd;
}

function lastWeek() {
  var d = new Date();
  var dayOfMonth = d.getDate();
  d.setDate(dayOfMonth - 7);
  return d;
}

function lastMonth() {
  var d = new Date();
  var monthOfYear = d.getMonth();
  d.setMonth(monthOfYear - 1);
  return d;
}