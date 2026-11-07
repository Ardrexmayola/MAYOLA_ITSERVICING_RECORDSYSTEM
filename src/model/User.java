package model;

public record User(
    int UserID,
    String FullName,
    String Username,
    String Password,
    String Role,
    String Status
) {}