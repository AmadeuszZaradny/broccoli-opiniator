# zgłaszanie opinie do dietyodbrokula.pl

## Pobranie szczegółów użytkonika

Wszystkie dane zalogowanego użytkownika, które są sewowane przez backend można pobrać z użyciem zapytania: 
```
curl 'https://dietyodbrokula.pl/customer/section/load' \
                 -H 'accept: application/json' \
                 -H 'cookie: PHPSESSID=xyz' \
                 -H 'x-requested-with: XMLHttpRequest' \
                 --compressed
```

## Zgłoszenie opini

Aby zgłosić opinie potrzebujesz wysłać przykładowy request. 
```
curl 'https://dietyodbrokula.pl/rest/V1/customer/menu-diet/save-opinion' \
                     -H 'content-type: application/json' \
                     -H 'cookie: PHPSESSID=xyz' \
                     -H 'x-requested-with: XMLHttpRequest' \
                     --data-raw '{"opinion":{"menu_id":32582,"date":"2022-05-02","stars":4,"note":"Wszystko w porządku.","product_id":277,"meal_time_id":3,"order_item_id":1553929}}' \
                     --compressed

```

#### Jak wygenerowac body?

Nie znalazłem endpointa do pobierania tych danych. Jednak gdy wejdzie się na formularz ocen pod adresem `https://dietyodbrokula.pl/customer/diets/rate/` to znajduję się tam formularz, który posiada w HTMLu ukryte inputy zawierające pola:
```
<form class="diet-rate__form" action="#" method="post" novalidate="novalidate">
  <input type="hidden" name="product_id" value="277">
  <input type="hidden" name="order_item_id" value="1553929">
  <input type="hidden" name="menu_id" value="32582">
  <input type="hidden" name="date" value="2022-05-02">
  <input type="hidden" name="meal_time_id" value="3">
                                    
   <div>...</div>
</form>
```

Można pobrać HTML i sparsować go np: z użyciem Jsoup:

```
curl 'https://dietyodbrokula.pl/customer/diets/rate/' \
                                -H 'accept: application/json' \
                                -H 'cookie: PHPSESSID=xyz' \
                                -H 'x-requested-with: XMLHttpRequest' \
                                --compressed
```

## Jak jak się zautoryzować?

Do autoryzacji używany jest cookie header z podanym id sesji np: `cookie: PHPSESSID=xyz`. Można go pobrać z nagłówka `set-cookie` z odpowiedzi na request:

```
curl 'https://dietyodbrokula.pl/customer/account/loginPost/referer/aHR0cHM6Ly9kaWV0eW9kYnJva3VsYS5wbC9jdXN0b21lci9hY2NvdW50L2luZGV4Lw%2C%2C/' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'cookie: PHPSESSID=qca4fjt2l9vseririo9u285728;' \
  --data-raw 'form_key=JBYogUnAlARmu2nW&login%5Busername%5D=${encoded_value}&login%5Bpassword%5D=${encoded_$value}' \
  --compressed -v
```  
