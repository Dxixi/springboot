package com.chixing.dao;

import com.chixing.entity.Note;
import com.chixing.entity.NoteExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NoteDao继承基类
 */
@Repository
@Mapper
public interface NoteDao extends MyBatisBaseDao<Note, Integer, NoteExample> {
    public Note selectOneOrderByCreateTimeDescByCustId(int custId);
    public List<Note> selectOrderByCreateTimeDescTenRecords();
    public List<Note> selectMyNoteList(int custId);
    public int selectCount();

    public List<Note> selectAll();



}