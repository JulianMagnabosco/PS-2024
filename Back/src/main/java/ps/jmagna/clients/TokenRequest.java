package ps.jmagna.clients;


public record TokenRequest (
    String client_id,
    String client_secret,
    String grant_type
){}
