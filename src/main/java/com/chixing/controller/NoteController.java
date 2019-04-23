package com.chixing.controller;

import com.chixing.common.JsonResult;
import com.chixing.entity.Customer;
import com.chixing.entity.Note;
import com.chixing.service.CustomerService;
import com.chixing.service.NoteService;
import com.chixing.util.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping("goToAdd")
    public String goToAdd(){
        return "note/add";
    }

    //游记头图上传
    @RequestMapping("headImgUpload")
    @ResponseBody
    public JsonResult upload(HttpServletRequest request, MultipartFile upload){
        System.out.println("upload 文件上传操作处理");
        String path = request.getServletContext().getRealPath("/upload");
        System.out.println("path==="+path);
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        String fileName = upload.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        fileName = uuid+"_"+fileName;
        System.out.println("fileName==="+fileName);
        File uploadFile = new File(file,fileName);
        try {
            upload.transferTo(uploadFile);
            // 返回json :当前上传文件在tomcat服务器中的相对路径
            Map<String,Object> data = new HashMap<>();
            data.put("filePath","upload/"+fileName);
            return JsonResult.createSuccessJsonResult(data);
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.createFailJsonResult(null);
        }
    }

    @RequestMapping("addNote")
    @ResponseBody
    public JsonResult addNote(Note note, HttpServletRequest request){
        System.out.println("添加游记");
        //注意添加游记要关联作者id,即当前登录用户的id,还有当前游记创建的时间
       int custId = (int) request.getSession().getAttribute("customerId");
       System.out.println("custId:"+custId);
       note.setCustId(custId);
       note.setNoteCreateTime(new Date());
        System.out.println("note:"+note);
       boolean flag = noteService.save(note);
        System.out.println("save note:"+note);
        System.out.println("add note flag==="+flag);
       if(flag){
            return JsonResult.createSuccessJsonResult(null);
       }else {
            Map<String,Object> data = new HashMap<>();
            data.put("reason","添加游记失败");
            return JsonResult.createFailJsonResult(data);
       }
    }

    @RequestMapping("goToDetail")
    public String goToDetail(){
        return "note/detail";
    }

    //查看我最近的游记（刚写的游记）
    @RequestMapping("myLastNote")
    @ResponseBody
    public JsonResult getMyLastNote(HttpServletRequest request){
        System.out.println(" getMyLast 查看我最近的游记");
        int custId = (int) request.getSession().getAttribute("customerId");
        Note note = noteService.getLastNote(custId);
        System.out.println("note:"+note);
        Map<String,Object> data = new HashMap<>();
        data.put("myLastNote",note);
        return JsonResult.createSuccessJsonResult(data);
    }


    //首页中获得最新的前10条游记对象
    @RequestMapping("getLastTen")
    @ResponseBody
    public JsonResult getLastTen(){
        List<Note> noteList = noteService.getLastTen();
        for(Note note:noteList){
            System.out.println("Note"+note);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("notelist",noteList);
        return JsonResult.createSuccessJsonResult(data);
    }
    @RequestMapping("goToMyList")
    public String goToMyList(){
        System.out.println("goToMyList");
        return "note/mylist";
    }
    //我的游记列表
    @RequestMapping("geMyList")
    @ResponseBody
    public JsonResult geMyList(HttpServletRequest request){
        int customerId = (int) request.getSession().getAttribute("customerId");
        List<Note> noteList = noteService.getMyList(customerId);
        Map<String,Object> data = new HashMap<>();
        if(noteList!=null&&noteList.size()>0){
            Customer customer = customerService.getByCustId(customerId);
            //数据中再绑定 customer  (昵称，城市，手机，...)
            data.put("noteList",noteList);
            data.put("customer",customer);
            return JsonResult.createSuccessJsonResult(data);
        }else {
            data.put("reason","暂无游记");
            return JsonResult.createFailJsonResult(null);
        }
    }
    @RequestMapping("goToDescriptionById/{noteId}")
    public String goToDescriptionById(@PathVariable("noteId")int noteId){
        System.out.println("noteId="+noteId);
        return "note/description.html?noteId="+noteId;
    }


    //根据noteId查看游记
    @RequestMapping("getByNoteId/{noteId}")
    @ResponseBody
    public JsonResult getById(@PathVariable("noteId") int noteId ){
        Note note = noteService.getByNoteId(noteId);
        Map<String,Object> data = new HashMap<>();
        data.put("note",note);
        return JsonResult.createSuccessJsonResult(data);
    }
    @RequestMapping("goPage")
    public String goPage(){
        return "page";
    }


    //分页
    @RequestMapping("getAll/{pageNum}")
    @ResponseBody
    public JsonResult getAllByPage(@PathVariable("pageNum") int pageNum){
        System.out.println("getAllByPage=======");
        // 5条游记
        List<Note> noteList = noteService.getAll(pageNum);// 当前页码的游记集合对象
        int totalCount = noteService.getCount();//游记总记录数
        int pageSize = PageHelperUtil.PAGE_SIZE;
        int totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;//共几页
        System.out.println("noteList======="+noteList);
        //游记作者的信息
        for (Note note:noteList){
            Customer customer = this.customerService.getByCustId(note.getCustId());
            note.setOthers1(customer.getCustName());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("noteList",noteList);
        data.put("totalCount",totalCount);
        data.put("totalPage",totalPage);
        data.put("pageSize",pageSize);
        return JsonResult.createSuccessJsonResult(data);
    }

}

