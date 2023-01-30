$('.table').on('click', 'tbody tr', function (e) {
    var parentNode = $(e.target).parent();

    var class_val = $(parentNode).attr('class');
    var id = $(parentNode).children('td').eq(0).html();
    if(class_val == 'youtube'){
        var result = youtube_find(id);
        $('#youtubeId').val(result.id);
        $('#youtubeDate').val(result.time);
        $('#youtubeURL').val(result.url);
        $('.modal-title').text('유튜브 수정')
        $('.modal-footer').html('<button type="button" id="YupdateBtn" class="btn btn-primary">저장</button><button type="button" id="YremoveBtn" class="btn btn-danger">삭제</button><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>');
    } else if(class_val === 'timeline'){

    }
    $('#modalfade').modal('show');
})

