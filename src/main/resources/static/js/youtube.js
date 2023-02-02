$('.table').on('click', 'tbody tr', function (e) {
    var tr = $(e.target).parent();
    var id = $(tr).children('td').eq(0).html();
    var start = $(tr).children('td').eq(1).html();
    var time =  $(tr).children('td').eq(3).html();
    console.log(id, start, time);
    var result = youtube_find(id);
    copy(timeline_find(start, time));
    location.href=result.url;
});

function copy(text){
    var $textarea = document.createElement("textarea");
    document.body.appendChild($textarea);

    $textarea.value = text;
    $textarea.select();

    document.execCommand('copy');
    document.body.removeChild($textarea);
}

$(document).on('click','.YmodifyBtn', function (e){
    var parentNode = $(e.target).parent().parent();
    var id = $(parentNode).children('td').eq(0).html();
    var result = youtube_find(id);
    $('.modal-title').text('유튜브 수정')
    $('.modal-body').html('<input type="hidden" id="youtubeId" name="id"><input type="text" id="youtubeDate" name="date" class="form-control" placeholder="녹화를 시작한 시각(녹화파일이름 기준)"><input type="text" id="youtubeURL" name="url" class="form-control" placeholder="유튜브 링크">');
    $('.modal-footer').html('<button type="button" id="YupdateBtn" class="btn btn-primary">저장</button><button type="button" id="YremoveBtn" class="btn btn-danger">삭제</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>');
    $('#youtubeId').val(result.id);
    $('#youtubeDate').val(result.time);
    $('#youtubeURL').val(result.url);
    $('#modalfade').modal('show');
});

$(document).on('click','#YinsertBtn', function (e){
    $('#actionForm').removeAttr('onsubmit');
    $('#actionForm').attr('action', '/manage/insert');
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$(document).on('click','#YupdateBtn',function (e){
    $('#actionForm').removeAttr('onsubmit');
    $('#actionForm').attr('action', '/manage/update')
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$(document).on('click','#YremoveBtn',function (e){
    $('#actionForm').removeAttr('onsubmit');
    $('#actionForm').attr('action', '/manage/remove')
    $('#actionForm').attr('method','post');
    $('#actionForm').submit();
});

$('#showInsertBtn').on('click',function(e){
    $('#youtubeId').val('')
    $('#youtubeDate').val('')
    $('#youtubeURL').val('')
    $('.modal-title').text('유튜브 등록')
    $('.modal-body').html('<input type="text" id="youtubeDate" name="date" class="form-control" placeholder="녹화를 시작한 시각(녹화파일이름 기준)"><input type="text" id="youtubeURL" name="url" class="form-control" placeholder="유튜브 링크">');
    $('.modal-footer').html('<button type="button" id="YinsertBtn" class="btn btn-primary">저장</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>');
});

