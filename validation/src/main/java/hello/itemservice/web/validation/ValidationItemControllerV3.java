package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }





    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
//        @Validated 하나로 검증기 수행 가능. (검증기를 실행해라 라는 의미)
//        만약 검증기가 여러개이면 supports메서드로 구분이 가능함.
//        근데 support도 내가 호출하는게 아니라 자동으로 호출되어서 가능한 검증기를 뽑이냄.
//
//        validated는 validdator를 자동으로 등록하는 역할을 한다.
//        @valid 를써도됨 => 자바표준
//        이렇게 어노테이션 기반 검증은 글로벌 발리데이터를 지워야함.

//        값이 제대로 들어와야 validated 검증을 시도한다.
//        @Modelattribute에 담고 타입체크를 한 후에 바인딩에 성공해야함.
//        ex) typeMismatch FieldError 추가 => errors.properties
//
//
//        어노테이션 기반
//        스프링 부트는 자동으로 글로벌 Validator로 등록한다.
//        LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다. 이 Validator는 @NotNull 같은
//        애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid ,
//        @Validated 만 적용하면 된다.
//                검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.



//        @InitBinder
//        public void init(WebDataBinder dataBinder){
//            dataBinder.addValidators(itemValidator);
//        } 를 컨트롤러 클래스에 넣어줘야함.

//        글로벌하게 전역컨트롤러에서 적용시켜주고 싶을때는
//        implements WebMvcConfiguration 을 적어주고
//        public Validator getValidator(){
//              return new ItemValidator
//       }
//        를 적어주면 전역에 적용이 된다.
//        대신에 저 @InitBinder 가 붙은 메서드는 주석처리를 하거나 지워줘야함. (전역에 적용하는 경우에만)
//        @Validated 는 있어야함.



        //@스크립트 어노테이션 쓰지말것. 이렇게 자바코드로 직접검증증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("{}",bindingResult);
            //이렇게 로그로 뽑아보면 notBlank.itemName .. 등등 오류코드 이름이 뜸 (에러코드 이름과 조합해서 뜸)
            /** errors.properties
             * #Bean Validation 추가
             * NotBlank={0} 공백X
             * Range={0}, {2} ~ {1} 허용
             * Max={0}, 최대 {1}
             */

            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }







    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("{}",bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

