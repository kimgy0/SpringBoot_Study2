package hello.itemservice.web.validation;


import hello.itemservice.domain.item.Item;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//컨트롤러에 지저분한 검증로직을 분리해낸다.
//그럼 컨트롤러의 코드가 더 깔끔해지겠지?
/*
 * 굳이 implemets validator 가 아니여도 그냥 갖다가 메서드만 써도 된다.
 * 근데 왜 validator 인터페이스를 이용해서 검증기를 만드는가?
 *
 * 추가적인 도움을 받을 수 잇기 떄문문 */
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item == clazz 아이템은 클래스로 넘어오는 아이템 타입과 같냐? 라는 코드
        //item에 자식이 있으면 자식클래스일때도 통과하면서 검증된다.

        //== 보다는 isAssignableFrom을 사용하는게 더 좋다. ( 자식클래스까지 다 커버 )
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        BindingResult bindingResult = (BindingResult) errors;

//        이렇게 타입캐스팅 하고 나서 밑에다가 검증로직 복사붙여넣기!
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null, "상품 이름은 필수 입니다."));
            bindingResult.rejectValue("itemName", "required");

//            ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
//            이 문장이 위의 if문을 대신해줌 - > 실제 타고 들어가도 if를 똑같이 구성해주고 rejectvalue까지 코드가 똑같음.

        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >=9999){
//            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, "최대 수량은 9,999 까지만 허용합니다."));
            bindingResult.rejectValue("quantity", "max", new Object[]{9999},null);

        }

        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
