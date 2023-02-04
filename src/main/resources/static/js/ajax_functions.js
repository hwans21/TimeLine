function youtube_find(id) {
	var result;
  	$.ajax({
        data: {'youtubeId': id},
        url: '/manage/find',
        dataType: 'json',
        async: false,
        success: function(data) {
      	    result = data;
        }
  	});
  	return result;
}

function timeline_insert(time,title){
    console.log(time,title);
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'time':time,
            'title':title
            }),
        url: '/timeline/insert',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            console.log(data)
        }
    });
}

function timeline_copyfind(start, time){
    console.log(start,time);
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'start':start,
            'time':time
        }),
        url: '/timeline/copylist',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            console.log(data)
            result = data;
        }
    });
    return result;
}

function timeline_getList(start,end,order, pageNum){
    console.log(start,end,order, pageNum);
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'start':start,
            'end':end,
            'order':order,
            'pageNum':pageNum
        }),
        url: '/timeline/list',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            result = data;
//            var tabledata = "";
//            $.each(list, function(i){
//                tabledata += `<tr><td>`+list[i].timeFormat+`</td><td>`+list[i].timelineCount+
//                            `</td><td>`+list[i].timelineTitle+`</td></tr>`
//
//            });
//            $('#timelineTable').html(tabledata);
        }
    });
    return result;

}

function countup(id,url){
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'id':id
        }),
        url: '/timeline/countup',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            result = data;
            location.href=url

        }
    });
}
