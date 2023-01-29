$('#youtubeInsertBtn').on('click',function (e){
    $('#youtubeInsertForm').submit();
});

$('#youtubeUpdateBtn').on('click',function (e){
    $('#youtubeUpdateForm').attr('action', '/youtube_update')
    $('#youtubeUpdateForm').submit();
});

$('#youtubeRemoveBtn').on('click',function (e){
    $('#youtubeUpdateForm').attr('action', '/youtube_remove')
    $('#youtubeUpdateForm').submit();
});