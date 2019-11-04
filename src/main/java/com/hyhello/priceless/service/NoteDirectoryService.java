package com.hyhello.priceless.service;

import com.hyhello.priceless.constants.NoteDirectoryOpStatus;
import com.hyhello.priceless.dataaccess.entity.NoteDirectory;
import com.hyhello.priceless.dataaccess.repository.NoteDirectoryRepository;
import com.hyhello.priceless.dto.DirectoryNode;

import static com.hyhello.priceless.utils.JavaUtils.*;

import com.hyhello.priceless.utils.JavaUtils;
import com.hyhello.priceless.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO
 * 事务
 * lazy load
 */
@Service
public class NoteDirectoryService {

    private NoteDirectoryRepository repository;

    @Autowired
    public NoteDirectoryService(NoteDirectoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void update(List<DirectoryNode> roots) throws IOException {
        List<NoteDirectory> directories = new ArrayList<>();
        //List<DirectoryNode> roots = JsonUtils.toJsonList(dir, DirectoryNode.class);
        roots.forEach(r -> iterGet(directories, r, 0));

        repository.saveAll(directories);
    }

    //懒加载
    public List<DirectoryNode> get() {
        List<NoteDirectory> directories = repository.findAll();
        Map<String, DirectoryNode> nodesMap = directories
                .stream()
                .map(NoteDirectoryService::directory2Node)
                .collect(Collectors.toMap(DirectoryNode::getKey, Function.identity()));
        Map<String, List<DirectoryNode>> childrenMap = new HashMap<>();

        nodesMap.forEach((k, v) -> {
            String parentKey = v.getParentKey();
            List<DirectoryNode> children = childrenMap.get(k);
            if (null == children) {
                children = new ArrayList<>();
                childrenMap.put(parentKey, children);
            }
            children.add(v);
        });

        List<DirectoryNode> result = new ArrayList<>();

        // find directories which parentId = 0
        List<DirectoryNode> roots = childrenMap.get("0");
        if (roots != null){
            roots.forEach( r -> {
                iterSet(childrenMap, r);
                result.add(r);
            });
        }

        return result;
    }

    public NoteDirectoryOpStatus newNode(int parentId, String title, boolean isFolder){
        NoteDirectory noteDirectory = new NoteDirectory();
        if (!isFolder){
            //TODO 创建 NOTE
            //noteDirectory.setNoteId();
        }
        noteDirectory.setTitle(title);
        noteDirectory.setParentId(parentId);
        noteDirectory.setIsFolder(JavaUtils.parseBoolean2Byte(isFolder));
        noteDirectory.setExpanded(JavaUtils.parseBoolean2Byte(false));
        noteDirectory.setStatus((byte) 1);
        noteDirectory.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(noteDirectory);

        return NoteDirectoryOpStatus.SUCCESS;
    }

    public NoteDirectoryOpStatus renameNode(int nodeId, String newName){
        Optional<NoteDirectory> optionalNoteDirectory = repository.findById(nodeId);
        if (!optionalNoteDirectory.isPresent()){
            return NoteDirectoryOpStatus.NOT_EXIST_NODE;
        }
        NoteDirectory noteDirectory = optionalNoteDirectory.get();
        noteDirectory.setTitle(newName);
        repository.save(noteDirectory);

        return NoteDirectoryOpStatus.SUCCESS;
    }

    public NoteDirectoryOpStatus deleteNode(int nodeId){
        Optional<NoteDirectory> optionalNoteDirectory = repository.findById(nodeId);
        if (!optionalNoteDirectory.isPresent()){
            return NoteDirectoryOpStatus.SUCCESS;
        }

        NoteDirectory noteDirectory = optionalNoteDirectory.get();
        if (JavaUtils.parseNumber2Boolean(noteDirectory.getIsFolder())){
            List<NoteDirectory> children = repository.findNoteDirectoriesByParentIdEquals(nodeId);
            if (!children.isEmpty()){
                return NoteDirectoryOpStatus.REMOVE_NOT_EMPTY_FOLDER;
            }
        }else {
            //删除文件

        }
        repository.delete(noteDirectory);
        return NoteDirectoryOpStatus.SUCCESS;
    }

    // directoryNode 转 noteDirectory
    private void iterGet(List<NoteDirectory> directories, DirectoryNode node, int parentId) {

        NoteDirectory directory = new NoteDirectory();
        Object id = node.getData().get("id");
        if (null != id){
            directory.setId((Integer)id);
        }
        directory.setTitle(node.getTitle());
        directory.setNoteId(Integer.valueOf(node.getKey()));
        directory.setExpanded(parseBoolean2Byte(node.isExpanded()));
        directory.setIsFolder(parseBoolean2Byte(node.isFolder()));
        directory.setParentId(parentId);
        Timestamp createTime = Timestamp.valueOf(LocalDateTime.parse((String) node.getData().get("createTime")));
        directory.setCreateTime(createTime);
        directories.add(directory);

        DirectoryNode[] children = node.getChildren();
        if (children != null) {
            for (DirectoryNode child : children) {
                iterGet(directories, child, Integer.valueOf(node.getKey()));
            }
        }
    }

    /**
     *  noteDirectory 转 directoryNode
     */
    private void iterSet(Map<String, List<DirectoryNode>> childrenMap, DirectoryNode node){
        List<DirectoryNode> children = childrenMap.get(node.getKey());
        if (children != null && !children.isEmpty()) {
            DirectoryNode[] nodeArr = new DirectoryNode[children.size()];
            node.setChildren(children.toArray(nodeArr));
            for (DirectoryNode child : children) {
                iterSet(childrenMap, child);
            }
        }
    }

    // 数据库实体转前端目录树 Node节点, 忽略children属性
    private static DirectoryNode directory2Node(NoteDirectory noteDirectory) {
        DirectoryNode node = new DirectoryNode();
        node.getData().put("id", noteDirectory.getId());
        node.setTitle(noteDirectory.getTitle());
        node.setKey(String.valueOf(noteDirectory.getId()));
        node.setExpanded(parseNumber2Boolean(noteDirectory.getExpanded()));
        node.setFolder(parseNumber2Boolean(noteDirectory.getIsFolder()));
        node.getData().put("createTime", noteDirectory.getCreateTime().toLocalDateTime());
        node.setParentKey(String.valueOf(noteDirectory.getParentId()));
        return node;
    }

}
