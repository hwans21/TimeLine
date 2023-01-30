$(document).on('click','#YinsertBtn', function (e){
    $('#actionForm').attr('action', '/manage/insert');
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$(document).on('click','#YupdateBtn',function (e){
    $('#actionForm').attr('action', '/manage/update')
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$(document).on('click','#YremoveBtn',function (e){
    $('#actionForm').attr('action', '/manage/remove')
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$('#showInsertBtn').on('click',function(e){
    $('#youtubeId').val('')
    $('#youtubeDate').val('')
    $('#youtubeURL').val('')
    $('.modal-title').text('유튜브 등록')

    $('.modal-footer').html('<button type="button" id="YinsertBtn" class="btn btn-primary">저장</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>');
});