package fsa.training.ims_team01.enums.commonEnum;

public enum OfferStatusEnum {
    WAITING_FOR_APPROVAL, //When the offer is created
    APPROVED_OFFER, //When Manager or Admin approves the offer
    REJECTED_OFFER, //When Manager or Admin rejects the offer
    WAITING_FOR_RESPONSE, // When the offer is sent and waiting candidate to response
    ACCEPTED_OFFER, //When candidate accepts the offer
    DECLINED_OFFER, //When candidate declines the offer
    CANCELLED //When offer is cancelled
}
