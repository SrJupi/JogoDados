package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistException extends RuntimeException{

    @Serial
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    private String searchedResource;
    private String searchedField;
    private String idUsed;

    public ResourceAlreadyExistException(String searchedResource, String searchedField, String idUsed) {
        super(String.format("%s with %s '%s' already exist", searchedResource, searchedField, idUsed));
        this.searchedResource = searchedResource;
        this.searchedField = searchedField;
        this.idUsed = idUsed;
    }

}
