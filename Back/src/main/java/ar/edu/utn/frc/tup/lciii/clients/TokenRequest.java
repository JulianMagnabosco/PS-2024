package ar.edu.utn.frc.tup.lciii.clients;


public record TokenRequest (
    String client_id,
    String client_secret,
    String grant_type
){}
