$('.table').on('click', 'tbody tr', function (e) {
    var parentNode = $(e.target).parent();

    var class_val = $(parentNode).attr('class');
    var id = $(parentNode).children('td').eq(0).html();
    if(class_val == 'youtube'){
        var result = youtube_find(id);
        $('#modYoutubeId').val(result.id);
        $('#modYoutubeDateTxt').val(result.time);
        $('#modYoutubeUrlTxt').val(result.url);
    } else if(class_val === 'timeline'){

    }
    $('#modifyModal').modal('show');
})

