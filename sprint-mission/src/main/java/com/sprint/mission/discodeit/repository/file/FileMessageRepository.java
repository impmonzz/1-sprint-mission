package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository extends SimpleJpaRepository<Message, UUID> implements MessageRepository {

    private final EntityManager entityManager;

    public FileMessageRepository(EntityManager em) {
        super(Message.class, em);
        this.entityManager = em;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return this.entityManager
                .createQuery("SELECT m FROM Message m WHERE m.channelId = :channelId", Message.class)
                .setParameter("channelId", channelId)
                .getResultList();
    }
}
