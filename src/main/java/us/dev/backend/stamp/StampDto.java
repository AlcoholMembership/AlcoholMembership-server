package us.dev.backend.stamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
