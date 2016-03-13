package pt.talkdesk.callBilling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.talkdesk.callBilling.bean.CallDetail;

public interface CallRepository extends JpaRepository<CallDetail, String> {

	List<CallDetail> findByAccountId(String accountId);
}
