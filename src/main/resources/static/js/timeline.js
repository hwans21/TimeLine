$('#showtimeLineBtn').on('click', function(e){
    console.log('timeline');
    $('.modal-title').text('타임라인 등록');
    $('.modal-body').html('<input type="hidden" id="currentTime" name="currentTime"><input type="text" id="timelineTxt" name="timeline" class="form-control" placeholder="타임라인 제목을 입력하세요">');
    $('.modal-footer').html('<button type="button" id="TinsertBtn" class="btn btn-primary">저장</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>');
    $('#currentTime').val(currentTime)
});

$(document).on('click','#TinsertBtn', function(e){
    var time = $('#currentTime').val();
    var title = $('#timelineTxt').val();
    timeline_insert(time, title);
    $('#modalfade').modal('hide');
});
$(document).on('keydown','#timelineTxt',function(e){
    if(e.keyCode === 13){
        var time = $('#currentTime').val();
        var title = $('#timelineTxt').val();
        timeline_insert(time, title);
        $('#modalfade').modal('hide');
    }
});

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