package pt.talkdesk.callBilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.talkdesk.callBilling.bean.ClientDetail;

public interface ClientRepository extends JpaRepository<ClientDetail, String> {

}
