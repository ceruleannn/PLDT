package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.Note;
import com.hyhello.priceless.dataaccess.entity.NoteDirectory;
import com.hyhello.priceless.dataaccess.repository.NoteDirectoryRepository;
import com.hyhello.priceless.dto.DirectoryNode;

import static com.hyhello.priceless.utils.JavaUtils.*;

import com.hyhello.priceless.dto.resp.NoteDirectoryResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.utils.JavaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO
 * 事务
 * lazy loadDir
 * Save 未保存confirm
 * add seq updateTime
 */
@Service
public class NoteDirectoryService {

    private NoteDirectoryRepository directoryRepository;
    private NoteService noteService;

    @Autowired
    public NoteDirectoryService(NoteDirectoryRepository directoryRepository, NoteService noteService) {
        this.directoryRepository = directoryRepository;
        this.noteService = noteService;
    }

    @Transactional
    public void update(List<DirectoryNode> roots) throws IOException {
        List<NoteDirectory> entities = new ArrayList<>();
        roots.forEach(r -> nodes2Entities(entities, r, 0));
        directoryRepository.saveAll(entities);
    }

    public List<DirectoryNode> get() {
        List<NoteDirectory> directories = directoryRepository.findAll();
        Map<String, DirectoryNode> nodesMap = directories
                .stream()
                .map(NoteDirectoryService::entity2Node)
                .sorted(Comparator.comparing(node -> (LocalDateTime)node.getData().get("createTime")))
                .collect(Collectors.toMap(DirectoryNode::getKey, Function.identity(),(u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new));

        //<parentId, childrenList>
        Map<Integer, List<DirectoryNode>> childrenMap = new HashMap<>();

        nodesMap.forEach((k, v) -> {
            Integer parentId = v.getParentId();
            List<DirectoryNode> children = childrenMap.get(parentId);
            if (null == children) {
                children = new ArrayList<>();
                childrenMap.put(parentId, children);
            }
            children.add(v);
        });

        List<DirectoryNode> result = new ArrayList<>();

        // find directories which parentId = 0
        List<DirectoryNode> roots = childrenMap.get(0);
        if (roots != null){
            roots.forEach( r -> {
                Entities2Nodes(childrenMap, r);
                result.add(r);
            });
        }

        return result;
        //return result.stream().sorted(Comparator.comparing(node -> (LocalDateTime)node.getData().get("createTime"))).collect(Collectors.toList());
    }

    public Response renameNode(int nodeId, String newName){
        Optional<NoteDirectory> optionalNoteDirectory = directoryRepository.findById(nodeId);
        if (!optionalNoteDirectory.isPresent()){
            return NoteDirectoryResponse.NOT_EXIST_NODE;
        }
        NoteDirectory noteDirectory = optionalNoteDirectory.get();
        noteDirectory.setTitle(newName);
        directoryRepository.save(noteDirectory);

        return NoteDirectoryResponse.SUCCESS;
    }

    public Response deleteNode(int nodeId){
        Optional<NoteDirectory> optionalNoteDirectory = directoryRepository.findById(nodeId);
        if (!optionalNoteDirectory.isPresent()){
            return NoteDirectoryResponse.SUCCESS;
        }

        NoteDirectory noteDirectory = optionalNoteDirectory.get();
        if (JavaUtils.parseNumber2Boolean(noteDirectory.getIsFolder())){
            List<NoteDirectory> children = directoryRepository.findNoteDirectoriesByParentIdEquals(nodeId);
            if (!children.isEmpty()){
                return NoteDirectoryResponse.REMOVE_NOT_EMPTY_FOLDER;
            }
        }
        directoryRepository.delete(noteDirectory);
        return NoteDirectoryResponse.SUCCESS;
    }

    /**
     *     directoryNode 转 noteDirectory
     *     遇到新增节点时直接insert
     */

    private void nodes2Entities(List<NoteDirectory> directories, DirectoryNode node, int parentId) {

        NoteDirectory entity = new NoteDirectory();

        //新增节点
        Integer id = null;
        if ((id = (Integer) node.getData().get("id")) == null){
            Note note  = noteService.createNote();
            entity.setNoteId(note.getNoteId());
            entity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        }else { //更新节点
            entity.setNoteId(Integer.valueOf(node.getKey()));
            entity.setId(id);
            Timestamp createTime = Timestamp.valueOf(LocalDateTime.parse((String) node.getData().get("createTime")));
            entity.setCreateTime(createTime);
        }

        entity.setParentId(parentId);
        entity.setTitle(node.getTitle());
        entity.setIsFolder(parseBoolean2Byte(node.isFolder()));
        entity.setExpanded(parseBoolean2Byte(node.isExpanded()));
        directories.add(entity);

        DirectoryNode[] children = node.getChildren();
        if (children != null) {
            for (DirectoryNode child : children) {
                nodes2Entities(directories, child, id);
            }
        }
    }

    /**
     *  noteDirectory 转 directoryNode
     */
    private void Entities2Nodes(Map<Integer, List<DirectoryNode>> childrenMap, DirectoryNode node){
        List<DirectoryNode> children = childrenMap.get((Integer)node.getData().get("id"));
        if (children != null && !children.isEmpty()) {
            DirectoryNode[] nodeArr = new DirectoryNode[children.size()];
            node.setChildren(children.toArray(nodeArr));
            for (DirectoryNode child : children) {
                Entities2Nodes(childrenMap, child);
            }
        }
    }

    /**
     * 数据库实体转前端目录树 Node节点, 忽略children属性
     */
    private static DirectoryNode entity2Node(NoteDirectory noteDirectory) {
        DirectoryNode node = new DirectoryNode();
        node.getData().put("id", noteDirectory.getId());
        node.getData().put("createTime", noteDirectory.getCreateTime().toLocalDateTime());
        node.setTitle(noteDirectory.getTitle());
        node.setKey(String.valueOf(noteDirectory.getNoteId()));
        node.setExpanded(parseNumber2Boolean(noteDirectory.getExpanded()));
        node.setFolder(parseNumber2Boolean(noteDirectory.getIsFolder()));
        node.setParentId(noteDirectory.getParentId());
        return node;
    }

}
