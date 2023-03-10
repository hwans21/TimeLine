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

function timeline_oneselect(id){
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'id':id
        }),
        url: '/timeline/oneselect',
        contentType: 'application/json',
        dataType : "json",
        async: false,
        success: function(data) {
            result = data;
        },
        error : function(status, error) {
             alert('오류발생\nstatus : '+status+'\nerror : '+error);
        }
    });
    return result;
}

function timeline_insert(title){
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'title':title
            }),
        url: '/timeline/insert',
        contentType: 'application/json',
        async: false,
        success: function(data) {
            if(data!=='-1'){
                alert('타임라인 저장 성공');
            }else {
                alert('타임라인 저장 실패\ntime : '+time+'\ntitle : '+title);
            }
            result = data;
        },
        error : function(status, error) {
             alert('오류발생\nstatus : '+status+'\nerror : '+error);
        }
    });
    return result;
}

function timeline_copyfind(start, time){
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'start':start,
            'time':time
        }),
        url: '/timeline/copylist',
        contentType: 'application/json',
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
        contentType: 'application/json',
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
        contentType: 'application/json',
        async: false,
        success: function(data) {
            result = data;
            location.href=url
        }
    });
}

function youtube_insert(url, date){
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'url':url,
            'date':date
        }),
        url: '/manage/insert',
        contentType: 'application/json',
        async: false,
        success: function(data) {
            if(data==='성공'){
                alert('유튜브링크 저장 성공');
            } else if(data==='권한부족'){
                alert('권한이 부족합니다.');
            } else if(data==='실패'){
                alert('유튜브링크 저장 실패\nurl : '+url+'\ndate : '+date);
            }
        },
        error : function(status, error) {
             alert('오류발생\nstatus : '+status+'\nerror : '+error);
        }
    });
}
function youtube_update(id, url, date){
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'id':id,
            'url':url,
            'date':date
        }),
        url: '/manage/update',
        contentType: 'application/json',
        async: false,
        success: function(data) {
            if(data==='성공'){
                alert('유튜브링크 수정 성공');
            } else if(data==='권한부족'){
                alert('권한이 부족합니다.');
            } else if(data==='실패'){
                alert('유튜브링크 수정 실패\nurl : '+url+'\ndate : '+date);
            }
        },
        error : function(status, error) {
             alert('오류발생\nstatus : '+status+'\nerror : '+error);
        }
    });
}
function youtube_remove(id){
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'id':id
        }),
        url: '/manage/remove',
        contentType: 'application/json',
        async: false,
        success: function(data) {
            if(data==='성공'){
                alert('유튜브링크 삭제 성공');
            } else if(data==='권한부족'){
                alert('권한이 부족합니다.');
            } else if(data==='실패'){
                alert('유튜브링크 삭제 실패');
            }
        },
        error : function(status, error) {
             alert('오류발생\nstatus : '+status+'\nerror : '+error);
        }
    });
}

function custom_alert(data){

}
