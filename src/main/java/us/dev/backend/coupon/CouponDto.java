package us.dev.backend.coupon;

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
public class CouponDto {
    @NotNull
    private String qrid;
    @NotNull
    private LocalDateTime usingTime;
    @NotNull
    private String location;
}
