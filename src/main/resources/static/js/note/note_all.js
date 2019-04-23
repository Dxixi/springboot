var currentPage;//当前页码
function loadNote(pageNum) {
    currentPage = pageNum;
    var previousPage = pageNum-1;
    $.get("/springboot_demo/note/getAll/"+pageNum,{},function (result) {
        var noteList = result.data.noteList;
        var totalPage = result.data.totalPage;
        var totalCount = result.data.totalCount;
        var pageSize = result.data.pageSize;
        console.log("noteList==="+noteList);
        console.log("totalPage==="+totalPage);
        console.log("totalCount==="+totalCount);
        console.log("pageSize==="+pageSize);

        //上下页显示
        if(pageNum==1){
            $(".previous").hide();
        }else {
            $(".previous").show();
        }
        if (pageNum==totalPage){
            $(".next").hide();
        }else {
            $(".next").show();
        }

        //设置页码信息
        $(".totalPage").text(totalPage);
        $(".totalCount").text(totalCount);
        $(".currentPage").text(pageNum);
        //设置游记信息
        $("#hot_note ul li").remove();
        var ul = $("#hot_note ul");
        var ele = "<li>\n" +
            "        <div class=\"note_img\">\n" +
            "        <a href=\"\"><img src=\"images/02.JPG\" class=\"a_img\"></a>\n" +
            "        </div>\n" +
            "        <div class=\"note_detail\">\n" +
            "        <a href=\"\" class=\"note_title\"></a>\n" +
            "        <span>坐标：</span><span class='note_city'></span>\n" +
            "        <span>作者：</span><a href=\"\" class=\"note_author\"><span></span></a>\n" +
            "        </div>\n" +
            "    </li>";

        $.each(noteList, function (index, note) {
            ul.append(ele);
            var li = ul.children().eq(index);
            var note_title = li.children().find(".note_title");
            var note_city = li.children().find(".note_city");
            var note_author = li.children().find(".note_author");
            //渲染数据
            note_title.text(note.noteTitle);
            note_city.text(note.noteCity);
            note_author.text(note.others1);
        });

    });
}
loadNote(1);