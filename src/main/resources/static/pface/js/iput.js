var browserSupport = {
    placeholder: 'placeholder' in document.createElement('input')
}
//模拟placeholder
if( !browserSupport.placeholder){
    $('input[placeholder],textarea[placeholder]').each(function(){
        var that = $(this),
            text= that.attr('placeholder'),
            oldType; 
            
        if(that.val()===""){
            if(that.attr('type') != 'password'){
                that.val(text).addClass('placeholder');  
            }else{
                that.before('<span class="placeholder">请输入密码</span>');
            }
        }   
        that.focus(function(){
            //ie8下readonly依然可以上焦点的处理
            if(that.attr('readonly')){
                that.blur();
                return;
            }
            //清除span.placeholder
            that.prev("span.placeholder").remove();
            that.removeClass('placeholder');
            
            if(that.val()===text){   
                that.val("");   
            }   
            that.css("color","#000");
        }).blur(function(){
            if(that.val()===""){
                if(that.attr('type') != 'password'){   
                    that.val(text).addClass('placeholder');
                }else{
                    that.before('<span class="placeholder">请输入密码</span>');
                }
                that.css("color","#a7a7a7");
            //防止异常情况：当有placeholder类，且值不为空（代码设置值时容易出现）
            }else{
                that.removeClass('placeholder');
                that.css("color","#000");
            }   
            
        }).closest('form').submit(function(){   
            if(that.val() === text){   
                that.val('');   
            }   
        });   
    });
    $(document).on('click','span.placeholder',function(){
        $(this).next("[placeholder]").focus();
        //删除span.placeholder会在[placeholder]的focus中进行
    })
}