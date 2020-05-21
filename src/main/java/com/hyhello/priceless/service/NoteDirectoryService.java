package com.hyhello.priceless.service;

import com.hyhello.priceless.dataaccess.entity.Note;
import com.hyhello.priceless.dataaccess.entity.NoteDirectory;
import com.hyhello.priceless.dataaccess.repository.NoteDirectoryRepository;
import com.hyhello.priceless.dto.Node;

import static com.hyhello.priceless.utils.JavaUtils.*;

import com.hyhello.priceless.dto.resp.NoteDirectoryResponse;
import com.hyhello.priceless.dto.resp.Response;
import com.hyhello.priceless.utils.JavaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
 *
 * add seq updateTime seq上移和下移
 * (右键)当前note不再请求多次 切换/保存 Save 未保存confirm
 * data map??
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
    public Response update(List<Node> roots) throws IOException {
        List<NoteDirectory> entities = new ArrayList<>();
        roots.forEach(r -> nodes2Entities(entities, r, 0));
        directoryRepository.saveAll(entities);
        return NoteDirectoryResponse.SUCCESS;
    }

    public List<Node> get() {
        List<NoteDirectory> directories = directoryRepository.findAll();
        Map<String, Node> nodesMap = directories
                .stream()
                .map(NoteDirectoryService::entity2Node)
                .sorted(Comparator.comparing(node -> (LocalDateTime)node.getData().get("createTime")))
                .collect(Collectors.toMap(Node::getKey, Function.identity(),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new));

        //<parentId, childrenList>
        Map<Integer, List<Node>> childrenMap = new HashMap<>();

        nodesMap.forEach((k, v) -> {
            Integer parentId = v.getParentId();
            List<Node> children = childrenMap.get(parentId);
            if (null == children) {
                children = new ArrayList<>();
                childrenMap.put(parentId, children);
            }
            children.add(v);
        });

        List<Node> result = new ArrayList<>();

        // find directories which parentId = 0
        List<Node> roots = childrenMap.get(0);
        if (roots != null){
            roots.forEach( r -> {
                Entities2Nodes(childrenMap, r);
                result.add(r);
            });
        }

        return result;
        //return result.stream().sorted(Comparator.comparing(node -> (LocalDateTime)node.getData().get("createTime"))).collect(Collectors.toList());
    }

    public Response addNode(Node newNode){

        NoteDirectory entity = new NoteDirectory();
        Note note  = noteService.createNote();
        entity.setNoteId(note.getNoteId());
        entity.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        entity.setParentId(newNode.getParentId());
        entity.setTitle(newNode.getTitle());
        entity.setIsFolder(parseBoolean2Byte(newNode.isFolder()));
        entity.setExpanded(parseBoolean2Byte(newNode.isExpanded()));

        entity = directoryRepository.save(entity);

        List<Node> nodes = new ArrayList<>();
        nodes.add(entity2Node(entity));
        return new NoteDirectoryResponse(NoteDirectoryResponse.SUCCESS, nodes);
    }

    public Response renameNode(Node newNode){
        Optional<NoteDirectory> optionalNoteDirectory = directoryRepository.findById(Integer.valueOf((String)newNode.getData().get("id")));
        if (!optionalNoteDirectory.isPresent()){
            return NoteDirectoryResponse.NOT_EXIST_NODE;
        }
        NoteDirectory noteDirectory = optionalNoteDirectory.get();
        noteDirectory.setTitle(newNode.getTitle());
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
        Note note = noteService.getNote(noteDirectory.getNoteId());
        if (StringUtils.isEmpty(note.getText())){
            noteService.deleteNote(note.getId());
        }

        directoryRepository.delete(noteDirectory);
        return NoteDirectoryResponse.SUCCESS;
    }

    /**
     *     directoryNode 转 noteDirectory
     *     遇到新增节点时直接insert
     */

    private void nodes2Entities(List<NoteDirectory> directories, Node node, int parentId) {

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

        Node[] children = node.getChildren();
        if (children != null) {
            for (Node child : children) {
                nodes2Entities(directories, child, id);
            }
        }
    }

    /**
     *  noteDirectory 转 directoryNode
     */
    private void Entities2Nodes(Map<Integer, List<Node>> childrenMap, Node node){
        List<Node> children = childrenMap.get((Integer)node.getData().get("id"));
        if (children != null && !children.isEmpty()) {
            Node[] nodeArr = new Node[children.size()];
            node.setChildren(children.toArray(nodeArr));
            for (Node child : children) {
                Entities2Nodes(childrenMap, child);
            }
        }
    }

    /**
     * 数据库实体转前端目录树 Node节点, 忽略children属性
     */
    private static Node entity2Node(NoteDirectory noteDirectory) {
        Node node = new Node();
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
