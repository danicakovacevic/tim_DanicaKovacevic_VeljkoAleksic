package zus.model;

import java.time.LocalDate;

public class UserDto {

    private final int Id;

    private String userName;
    private String firstName;
    private String lastName;
    private String pass;
    private LocalDate CreatedAtDt;

    public UserDto(int id, String userName, String firstName, String lastName, String pass, LocalDate CreatedAtDt) {
        Id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pass = pass;
        this.CreatedAtDt = CreatedAtDt;
    }

    public int getId() {
        return Id;
    }
}
