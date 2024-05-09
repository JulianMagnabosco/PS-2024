package ps.jmagna.services;

import ps.jmagna.dtos.publication.CommentDto;
import ps.jmagna.dtos.publication.ListCommentsResponce;
import ps.jmagna.dtos.publication.CommentRequest;
import ps.jmagna.entities.CommentEntity;
import ps.jmagna.repository.CommentRepository;
import ps.jmagna.repository.PublicationRepository;
import ps.jmagna.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PublicationRepository publicationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Value("${app.url}")
    private String url;

    public CommentDto register(CommentRequest request){
        CommentEntity comment = new CommentEntity();
        comment.setPublication(publicationRepository.getReferenceById(request.getPub()));
        comment.setUser(userRepository.getReferenceById(request.getUser()));
        if(request.getFather()!=0){
            comment.setFather(commentRepository.getReferenceById(request.getFather()));
        }
        if(request.getGrandfather()!=0){
            comment.setGrandfather(commentRepository.getReferenceById(request.getGrandfather()));
        }
        comment.setText(request.getText());
        commentRepository.save(comment);
        return modelMapper.map(comment,CommentDto.class);
    }

    public ListCommentsResponce getAll(Long id){
        ListCommentsResponce responce = new ListCommentsResponce();
        List<CommentDto> list = new ArrayList<>();
        int countTotal=0;
        for (CommentEntity c : commentRepository.findAllByPublication_Id(id)) {
            if(c.getFather()==null){
//                if(c.isDeleted() && c.getChilds().isEmpty()){
//                    continue;
//                }

                List<CommentDto> childs = new ArrayList<>();
                for (CommentEntity cc : commentRepository.findAllByGrandfather_Id(c.getId())) {
//                    if(cc.isDeleted() && cc.getChilds().isEmpty()){
//                        continue;
//                    }
                    childs.add(new CommentDto(
                            cc.getId(),
                            cc.getPublication().getId(),
                            cc.getUser().getId(),
                            cc.getUser().getUsername(),
                            url + "/api/image/user/" + cc.getUser().getId(),
                            cc.getText(),
                            cc.getFather().getText(),
                            null,
                            cc.isDeleted()
                            ));
                    countTotal++;
                }

//                if(c.isDeleted() && childs.isEmpty()){
//                    continue;
//                }
                list.add(new CommentDto(
                        c.getId(),
                        c.getPublication().getId(),
                        c.getUser().getId(),
                        c.getUser().getUsername(),
                        url + "/api/image/user/" + c.getUser().getId(),
                        c.getText(),
                        null,
                        childs,
                        c.isDeleted()
                ));
                countTotal++;
            }
        }

        responce.setList(list);
        responce.setCountTotal(countTotal);
        return responce;
    }

    public boolean delete(Long id){
        CommentEntity comment = commentRepository.getReferenceById(id);
        comment.setDeleted(true);
        comment.setText("<Comentario eliminado>");
        commentRepository.save(comment);
        return true;
    }
}
