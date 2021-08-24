package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang="javascript", script = "_this.price*_this.quantity>=10000", message = "오류")
//오브젝트 오류는 scriptassert 어노테이션을 이용한다.
//하지만 실무에서는 범위를 초과할 때도 있고 해서 대응하기 어려움
//왠만하면 순수한 자바코드를 이용해서 검증하자
public class Item {

    private Long id;

    @NotBlank(message="공백x")
    private String itemName;

    @NotNull
    @Range(min=1000, max=1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
