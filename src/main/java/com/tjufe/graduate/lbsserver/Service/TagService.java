package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Tag;
import com.tjufe.graduate.lbsserver.Dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    TagDao tagDao;

    public Tag create(Tag tag) {
        // todo: check validity
        tagDao.save(tag);
        return tag;
    }

    public void delete(int tagId) {
        tagDao.deleteById(tagId);
    }

    public List<Tag> list() {
        return tagDao.findAll();
    }
}
