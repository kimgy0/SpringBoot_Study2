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
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {


    private final ItemValidator itemValidator;
    private final ItemRepository itemRepository;

    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
        /*
            컨트롤러 호출시에 마다 webdatabinder가 들어오는데 이렇게 검증기를 등록해놓으면 컨트롤러 메서드 호출시마다 검증 가능.
         */
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
            //첫번째 인자는 모델에 담기는 item이름이고 두번째는 필드이름 세번째는 에러메세지.

        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price","가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >=999){
            bindingResult.addError(new FieldError("item", "quantity", "최대 수량은 9,999 까지만 허용합니다."));
        }

        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10000원이 넘지 않습니다."));
                //오브젝트 에러는 뭔가를 넘겨줄때
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //리디렉트하고 나면 필드의 값이 자꾸 없어짐
    //그것을 유지하기 위해 fielderror 에 인자를 더추가한 방식.
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,null,null, "상품 이름은 필수 입니다."));
            /* v2로직에서는 리디렉트되면 값이 없어지는 현상을 고치려고 바인딩 인자를 추가해줌 저렇게
            *  세번째인자에 바인딩된값을 넣어주고
            *  false는 값이 잘 넘어왔으면 false로 지정해줌(변수에 값이 들어있는지!)
            *  그리고 나머지는 null null 로 구성
            * 마지막은 기본오류 메세지!
            *  */
            //첫번째 인자는 모델에 담기는 item이름이고 두번째는 필드이름 세번째는 에러메세지.

        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,null,null,"가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            /*
             * 예를 들어서 이부분이 qqq가 들어왔으면 타입체크가 안됨.
             * 그러면 item.getPrice()에 qqq가 들어가고 그다음 false말고 true를 자동으로 내삽한다음에
             * 뷰로 던져줌
             */
        }
        if(item.getQuantity() == null || item.getQuantity() >=999){
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,null,null, "최대 수량은 9,999 까지만 허용합니다."));
        }

        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item",null,null,"가격 * 수량의 합은 10000원이 넘지 않습니다."));
                //오브젝트 에러는 뭔가를 넘겨줄때
                //이거는 인자가 원래값이 없게 하나 생략.
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //리디렉트하고 나면 필드의 값이 자꾸 없어짐
    //그것을 유지하기 위해 fielderror 에 인자를 더추가한 방식.

    //+커스텀으로 메세지코드를 이용해서 메세지를 뽑아내려는 방식
    // properties를 설정해야함. (errors.properties)
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null, "상품 이름은 필수 입니다."));
            /**
             * 커스텀 에러출력
             *
             * string 배열로 하기되면 만약 여러개가 들어가도 ㄱㅊ (error.properties)의 키값을 넣어줌.
             * 인자가 필요없으니까 다음 값은 null
             *
             */


            /* v2로직에서는 리디렉트되면 값이 없어지는 현상을 고치려고 바인딩 인자를 추가해줌 저렇게
             *  세번째인자에 바인딩된값을 넣어주고
             *  false는 값이 잘 넘어왔으면 false로 지정해줌(변수에 값이 들어있는지!)
             *  그리고 나머지는 null null 로 구성
             * 마지막은 기본오류 메세지!
             *  */
            //첫번째 인자는 모델에 담기는 item이름이고 두번째는 필드이름 세번째는 에러메세지.

        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            /**
             * 이런식으로 인자를 object배열 형식으로 넘겨줌
             */

            /*
             * 예를 들어서 이부분이 qqq가 들어왔으면 타입체크가 안됨.
             * 그러면 item.getPrice()에 qqq가 들어가고 그다음 false말고 true를 자동으로 내삽한다음에
             * 뷰로 던져줌
             */
        }
        if(item.getQuantity() == null || item.getQuantity() >=999){
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, "최대 수량은 9,999 까지만 허용합니다."));
        }

        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000, resultPrice},"가격 * 수량의 합은 10000원이 넘지 않습니다."));
                //오브젝트 에러는 뭔가를 넘겨줄때
                //이거는 인자가 원래값이 없게 하나 생략.
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }








    //    이미 bindingResult 는 item객체를 알고 있고
    //    불필요하게 인자가 많을 필요는 없음 그렇기 때문에 더 간단하게 적을수 있는 reject value사용
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.addError(new FieldError("item", "itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null, "상품 이름은 필수 입니다."));
            bindingResult.rejectValue("itemName", "required");

//            ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
//            이 문장이 위의 if문을 대신해줌 - > 실제 타고 들어가도 if를 똑같이 구성해주고 rejectvalue까지 코드가 똑같음.


            /**
             * 필드이름을 적어주고 errorCode 는 error.properties에 규칙성을 띈다. required.---. -- =oooo
             *                                                              에러코드.객체이름.필드이름
             *
             *  처음엔 범용으로 적어주다가 require = oooo
             *  나중에 갈수록 세세하게 require.item.itemName 을 적어주면 나중에 이게 우선순위를 가지고 이걸 띄워준다.
             *
             */

            /**
             * 커스텀 에러출력
             *
             * string 배열로 하기되면 만약 여러개가 들어가도 ㄱㅊ (error.properties)의 키값을 넣어줌.
             * 인자가 필요없으니까 다음 값은 null
             *
             */


            /* v2로직에서는 리디렉트되면 값이 없어지는 현상을 고치려고 바인딩 인자를 추가해줌 저렇게
             *  세번째인자에 바인딩된값을 넣어주고
             *  false는 값이 잘 넘어왔으면 false로 지정해줌(변수에 값이 들어있는지!)
             *  그리고 나머지는 null null 로 구성
             * 마지막은 기본오류 메세지!
             *  */
            //첫번째 인자는 모델에 담기는 item이름이고 두번째는 필드이름 세번째는 에러메세지.

        }
        if(item.getPrice() == null || item.getPrice()<1000 || item.getPrice() > 1000000){
//            bindingResult.addError(new FieldError("item", "price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            bindingResult.rejectValue("price", "range", new Object[]{1000,1000000},null);

            /**
             * 이런식으로 인자를 object배열 형식으로 넘겨줌
             */

            /*
             * 예를 들어서 이부분이 qqq가 들어왔으면 타입체크가 안됨.
             * 그러면 item.getPrice()에 qqq가 들어가고 그다음 false말고 true를 자동으로 내삽한다음에
             * 뷰로 던져줌
             */
        }
        if(item.getQuantity() == null || item.getQuantity() >=9999){
//            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, "최대 수량은 9,999 까지만 허용합니다."));
            bindingResult.rejectValue("quantity", "max", new Object[]{9999},null);

        }

        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
//                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000, resultPrice},"가격 * 수량의 합은 10000원이 넘지 않습니다."));
                //오브젝트 에러는 뭔가를 넘겨줄때
                //이거는 인자가 원래값이 없게 하나 생략.
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }






//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if(itemValidator.supports(Item.class)){
            itemValidator.validate(item, bindingResult);
        }
//        if --- 문을 적고 참거짓을 뽑아낸다음.
        itemValidator.validate(item, bindingResult);


        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }






    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
//        @Validated 하나로 검증기 수행 가능. (검증기를 실행해라 라는 의미)
//        만약 검증기가 여러개이면 supports메서드로 구분이 가능함.
//        근데 support도 내가 호출하는게 아니라 자동으로 호출되어서 가능한 검증기를 뽑이냄.


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



        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }







    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

