package us.dev.backend.Stamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StampDto {
    @NotNull
    private String qrid;
    @NotNull
    private LocalDateTime saveTime;
    @NotNull
    private String location;
}
