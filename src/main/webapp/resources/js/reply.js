// $.ajax 사용 틀
// $.ajax({

//     type: 'post or get or put',
//     url: '요청할 url',
//     async: '기본값은 true', //true 비동기, false 동기 //딱히 건들일이 없다
//     data: 서버로 전송할 데이타... json타입으로 많이 사용,
//     contentType: "서버로 전송할 데이터 형식",
//     dataType: "서버에서 전송받을 데이터 형식",
//     success:function(data,status,xhr){
//         //성공하면 여기
//     },
//     error:function(xhr,status,error){
//         //실패하면 여기
//     }
// })


console.log("Reply Module...........");

var replyService =(function(){
    
    function add(reply, callback, error){
        console.log("reply.............");

        $.ajax({
            type : 'post',
            url : '/replies/new',
            data : JSON.stringify(reply),
            contentType : "application/json; charset=utf-8",
            success : function(result, status, xhr){
                if(callback){
                    callback(result);
                }
            },
            error : function(xhr, status, er){
                if(error){
                    error(er);
                }
            }
        })

    }

    function getList(param, callback, error){
        var bno = param.bno;
        var page = param.page || 1;

        $.getJSON("/replies/pages/" + bno + "/" + page + ".json",
        function(data){
            if(callback){
                callback(data);
            }
        }).fail(function(xhr, status, err){
            if(error){
                error();
            }
        });
        
    }
	
	function remove(rno, callback,error) {
		$.ajax({
		type:"delete",
		url : "/replies/" +rno,
		success :function(deleteResult, status, xhr) {
		if(callback) {
		callback(deleteResult);
		}
		},
		error : function(xhr,status,err) {
			if(error) {
			error(err);
			}
			}
			});
			}
			
	function update(reply, callback, error){

        console.log("RNO: " + reply.rno);

        $.ajax({
            type : 'put',
            url : '/replies/' + reply.rno,
            data : JSON.stringify(reply),
            contentType : "application/json; charset=utf-8",
            success : function(result, status, xhr){
                if(callback){
                    callback(result);
                }
            },
            error : function(xhr, status, er){
                if(error){
                    error(er);
                }
            }


        });

    } 
    
    function get(rno, callback, error) {
    $.get("/replies/" + rno + ".json", function(result) {
    	if(callback) {
    	callback(result);
    	}
    }).fail(function(xhr, status, err) {
    	if(error) {
    		error();
    		}
    	});
    } 	
    
  function displayTime(timeValue) {

      var today = new Date();
      var gap = today.getTime() - timeValue;

      var dateObj = new Date(timeValue);
      var str = "";

      if (gap < (1000 * 60 * 60 * 24)) {

         var hh = dateObj.getHours();
         var mi = dateObj.getMinutes();
         var ss = dateObj.getSeconds();

         return [ (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi,
               ':', (ss > 9 ? '' : '0') + ss ].join('');

      } else {
         var yy = dateObj.getFullYear();
         var mm = dateObj.getMonth() + 1; // getMonth() is zero-based
         var dd = dateObj.getDate();

         return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/',
               (dd > 9 ? '' : '0') + dd ].join('');
      }
   }
   
  function getList(param, callback, error) {

       var bno = param.bno;
       var page = param.page || 1;
       
       $.getJSON("/replies/pages/" + bno + "/" + page + ".json",
           function(data) {
          
             if (callback) {
               //callback(data); // 댓글 목록만 가져오는 경우 
               callback(data.replyCnt, data.list); //댓글 숫자와 목록을 가져오는 경우 
             }
           }).fail(function(xhr, status, err) {
         if (error) {
           error();
         }
       });
     }
   	
   
    return {
        add:add,
        getList : getList,
        remove : remove,
        update : update,
        get : get,
        displayTime : displayTime
    }
})();

