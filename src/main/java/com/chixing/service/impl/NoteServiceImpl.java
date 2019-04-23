package com.chixing.service.impl;

import com.chixing.dao.NoteDao;
import com.chixing.entity.Note;
import com.chixing.entity.NoteExample;
import com.chixing.service.NoteService;
import com.chixing.util.PageHelperUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    public NoteDao noteDao;
    //查看最近10条游记
    @Override
    public List<Note> getLastTen() {
        List<Note> noteList = noteDao.selectOrderByCreateTimeDescTenRecords();
        return noteList;
    }

    //添加游记
    @Override
    public boolean save(Note note) {
       System.out.println("NoteServiceImpl.....save(Note note)");
       int rows=noteDao.insert(note);
        System.out.println("rows==="+rows);
       return rows>0;
    }

    @Override
    public Note getLastNote(int custId) {
        Note note = noteDao.selectOneOrderByCreateTimeDescByCustId(custId);
        return note;
    }

    @Override
    public List<Note> getMyList(int custId) {
        List<Note> noteList = noteDao.selectMyNoteList(custId);
        return noteList;
    }

    /**
     * 根据noteId查看游记
     * @param noteId
     * @return
     */
    @Override
    public Note getByNoteId(int noteId) {
        NoteExample noteExample = new NoteExample();
        noteExample.createCriteria().andNoteIdEqualTo(noteId);
        List<Note> noteList = noteDao.selectByExample(noteExample);
        if(noteList!=null&&noteList.size()>0){
            return noteList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<Note> getAll(int pageNum) {
        PageHelper.startPage(pageNum,PageHelperUtil.PAGE_SIZE);//PageHelper拦截所有的select操作，分页
        System.out.println("pageNum"+pageNum);
        List<Note> noteList =this.noteDao.selectByExample(null);
        System.out.println("noteList"+noteList);
        return noteList;
    }

    @Override
    public int getCount() {
        return noteDao.selectCount();
    }

    @Override
    public List<Note> getNotes() {
        return null;
    }


}
