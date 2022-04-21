import java.util.*;

public class Main {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		for (; ;) {
			System.out.println("Введите требуемое действие и аргументы в формате A ? B");
			System.out.println("A, B -> целые числа в диапазоне от 1 до 10, допускается ввод только арабских или только римских цифр");
			System.out.println("? -> операция с числами, может быть +, -, *, или /");
			System.out.println("Все вычисления целочисленные, остатки от деления игнорируются");
			System.out.println("Для выхода из программы, введите exit");
			String raw_data = scanner.nextLine();
			if (raw_data.contains("exit")) {
				System.out.println("Программа завершена");
				break;
			}
			String success = calc (raw_data);
			if (success == "failed") {
				break;
			}
		}
		scanner.close();
	}
	
	

	public static String calc(String input) {
		String success = "";
		String cleaned_data = stringValidation(input);						// Валидация строки
		if (cleaned_data == "error" ) {
			System.out.println("DATA VALIDATION ERROR. Программа завершена");
			success = "failed";
		}
		else {
			String method = findMethods(cleaned_data);						// Вычленение методов
			if (method == "error") {
				System.out.println("METHOD VALIDATION ERROR. Программа завершена");
				success = "failed";
			}
			else {
				List<String> arguments = getAttrs(cleaned_data, method);  	// Вычленение операндов
				if (arguments.contains("error")) {
					System.out.println("ARGS VALIDATION ERROR. Программа завершена");
					success = "failed";
				}
				else {
					arguments.add(String.valueOf(method.charAt(0)));		
					String result = calculate(arguments);					// Вычисления
					if (result == "error") {
						System.out.println("OPERATION ERROR. Программа завершена");
						success = "failed";
					}
					else {
						System.out.println(result);
						success = "passed";
					}
				}
			}
		}
		
		return success;
	}
	
	
	
	public static String stringValidation(String raw_data) {
	// Проверка введенной строки на наличие невалидных смволов, приведение строки к UpperCase
		
		String valid_data = "0123456789IVX+-*/ ";	// элементы, допустимые на входе
		String cleaned_data = "";					// сюда соберется строка без пробелов
		String normalize_data = raw_data.toUpperCase();
		if (raw_data.equals(normalize_data) != true) {
			raw_data = normalize_data;
			System.out.println("Программа принимает ПРОПИСНЫЕ символы, ваша строка преобразована в " + raw_data);
		}
		for (int i = 0; i <= (raw_data.length()) - 1; i++) {			// перебираем посимвольно строку на входе
			String element = String.valueOf(raw_data.charAt(i));
			int index = valid_data.indexOf(element);
			if (index == -1) {											// если символ не входит в valid_data
				System.out.println("Найден недопустимый символ: " + element);
				return "error";
			}
			if (raw_data.charAt(i) != ' ') {
				cleaned_data += element;
			}
		}
		return cleaned_data;
	}
	
	
	
	public static String findMethods(String raw_data) {
		// Поиск операторов в строке, проверка их количества
		
		String [] valid_method = new String[] {"+", "-", "*", "/"};  	// типы операций, допустимые на входе
		String selected_method = "";									// текущая операция для поиска
		String finded_method = "";										// найденная операция
		int method_counter = 0;											// счетчик операций
		int method_pointer = 0;											// указатель позиции операции в строке
		for (int i = 0; i <= valid_method.length - 1; i++) {
			selected_method = valid_method[i];
			int pointer = raw_data.indexOf(selected_method);
			if (pointer != -1) {                                 				// если нашли операцию
				if (method_counter == 0) {
					method_pointer = pointer;
				}
				method_counter += 1;
				finded_method += selected_method;
				if (raw_data.indexOf(selected_method, pointer + 1) != -1) {    	// проверяем, не дублируется ли она
					method_counter += 1;								
				}
			}
			
		}
		if (method_counter > 1) {
			System.out.println("Метод должен быть только один!!");
			return "error";
		}
		else if (method_counter == 0) {
			System.out.println("Не указано ни одного метода!!");
			return "error";
		}
		return finded_method + method_pointer;
	}
	
	
	private static List<String> getAttrs(String cleaned_data, String method) {
		// Вычленяем аргументы и проверяем правильность
		
		String arg1 = "";									// аргументы
		String arg2 = "";
		String arg1_type = "";								// типы аргументов (римские цифры или арабские)
		String arg2_type = "";		
		String arg1_error = "";								// ошибка представления числа
		String arg2_error = "";
		String args_type = "";								// тип аргументов
		String target_symbols_a = "0123456789";				// для для определения типа текущего символа
		ArrayList<String> arguments = new ArrayList<String>();
		int pointer = Integer.valueOf(String.valueOf(method.charAt(1)));
		for (int i = 0; i < pointer; i++) {					// перебираем первый аргумент (до знака оператора)
			int index = target_symbols_a.indexOf(cleaned_data.charAt(i)); 	// проверяем на принадлежность к арабским цифрам
			if (index != -1) {
				if (arg1_type == "") {				// если текущий символ содержит арабскую цифру, флаг ставим "А"
					arg1_type = "A";
				}
				else if (arg1_type == "R") {
					System.out.println("Неверный формат числа, смешаны арабские и римские цифры");
					arg1_error = "error";
				}
											
			}
			else {
				if (arg1_type == "") {
					arg1_type = "R";
				}
				else if (arg1_type == "A") {
					System.out.println("Неверный формат числа, смешаны арабские и римские цифры");
					arg1_error = "error";
				}
				
			}
			arg1 += cleaned_data.charAt(i);
		}
		
		for (int i = pointer + 1; i < cleaned_data.length(); i++) {  	// перебираем второй аргумент
			int index = target_symbols_a.indexOf(cleaned_data.charAt(i));
			if (index != -1) {
				if (arg2_type == "") {									// если текущий символ содержит арабскую цифру,
					arg2_type = "A";									// и это первый символ аргумента,  флаг ставим "А"
				}
				else if (arg2_type == "R") {							// а если мы уже при проверке предыдущих символов
					System.out.println("Неверный формат числа, смешаны арабские и римские цифры");
					arg2_error = "error";								// выставляли флаг "R", то число неверно записано
				}
											
			}
			else {
				if (arg2_type == "") {									// здесь то же самое, но обработан вариант, что первым
					arg2_type = "R";									// символом числа будет римская цифра
				}
				else if (arg2_type == "A") {
					System.out.println("Неверный формат числа, смешаны арабские и римские цифры");
					arg2_error = "error";								
				}
				
			}
			arg2 += cleaned_data.charAt(i);
		}
		
		
		
		if (arg1_error == "error" | arg2_error == "error") {
			arguments.add("error");
			return arguments;
		}
		
		if (arg1 != "error" & arg2 != "error") {						// Если аргументы совпадают
			if (arg1_type == arg2_type) {
				args_type = arg1_type;
			}
			else {
				System.out.println("Аргументы не совпадают, числа могут быть либо только римскими, либо только арабскими");
				arguments.add("error");
				return arguments;
			}
		}
		
		if (args_type == "R") {											// Если флаг "R" - конвертируем римские цифры в арабские
			arg1 = convert_r_to_a(arg1);
			arg2 = convert_r_to_a(arg2);
		}
		
		if ((Integer.valueOf(arg1) < 1 | Integer.valueOf(arg1) > 10) | (Integer.valueOf(arg2) < 1 | Integer.valueOf(arg2) > 10)) {
		 	System.out.println("Аргументы должны быть заданы в диапазоне от 1 до 10 (от I до X)");
		 	arguments.add("error");
			return arguments;
		}
		
		arguments.add(arg1);
		arguments.add(arg2);
		arguments.add(args_type);
		return arguments;
	}

	
	
	
	private static String calculate(List<String> arguments) {
		// Расчеты
		
		String args_type = arguments.get(2);						// представление аргументов (римские или арабские цифры)
		int arg1 = Integer.parseInt(arguments.get(0));				// аргумент1
		int arg2 = Integer.parseInt(arguments.get(1));				// и 2
		String method = arguments.get(3);							// тип операции
		int res = 0;
		String result = "";
		switch(method) {
		case "+":
			res = arg1 + arg2;
			break;
		case "-":
			res = arg1 - arg2;
			break;
		case "*":
			res = arg1 * arg2;
			break;
		case "/":
			res = arg1 / arg2;
			break;
		}
		if (args_type == "R" & res < 1) {
			System.out.println("Ошибка вычисления, результат для римских чисел не может быть меньше I");
			return "error";
		}
		else {
			if (args_type == "R") {
				result = convert_a_to_r(res);
			}
			else {
				result = Integer.toString(res);
			}
			// System.out.println(result);
		}
		
		return result;
	}

	
	private static String convert_r_to_a(String raw_data) {
		// конвертор римских чисел в арабские (до 399 / I - CCCXCIX)
		
		int c = 0; 				// current - текущая цифра в числе
		int p = 0;				// previous - предыдущая цифра в числе
		int t = 0;				// temporary - временный сумматор
		int s = 0;				// sum - финальный сумматор
		
		Map<String, Integer> roman_values = new HashMap<String, Integer>();
		roman_values.put("I", 1);
		roman_values.put("V", 5);
		roman_values.put("X", 10);
		roman_values.put("L", 50);
		roman_values.put("C", 100);
		for (int i = 0; i <= (raw_data.length()) - 1; i++) {
			String current_char = String.valueOf(raw_data.charAt(i)); // выудили текущий символ из строки
			c = roman_values.get(current_char);
			if (c > p) {
				t = c - p;
				p = c;
			}
			else if (c == p) {
				t += c;
			}
			else if (c < p) {
				s += t;
				t = 0;
				t += c;
				p = c;
			}
		}
		s += t;
		return Integer.toString(s);
	}
	
	
	
	
	private static String convert_a_to_r(int data) {
		// конвертор арабских чисел в римские  (1 ~ 1000)
		
		
		String out_data = "";   // сюда запишется ответ
		Map<Integer, String> rank1 = new HashMap<Integer, String>();		// словарь для единиц
		rank1.put(1, "I");
		rank1.put(2, "II");
		rank1.put(3, "III");
		rank1.put(4, "IV");
		rank1.put(5, "V");
		rank1.put(6, "VI");
		rank1.put(7, "VII");
		rank1.put(8, "VIII");
		rank1.put(9, "IX");
		
		Map<Integer, String> rank2 = new HashMap<Integer, String>();		// словарь для десятков
		rank2.put(1, "X");
		rank2.put(2, "XX");
		rank2.put(3, "XXX");
		rank2.put(4, "XL");
		rank2.put(5, "L");
		rank2.put(6, "LX");
		rank2.put(7, "LXX");
		rank2.put(8, "LXXX");
		rank2.put(9, "XC");
		
		Map<Integer, String> rank3 = new HashMap<Integer, String>();		// словарь для сотен
		rank3.put(1, "C");
		rank3.put(2, "CC");
		rank3.put(3, "CCC");
		rank3.put(4, "CD");
		rank3.put(5, "D");
		rank3.put(6, "DC");
		rank3.put(7, "DCC");
		rank3.put(8, "DCCC");
		rank3.put(9, "CM");
		
		if (data == 10 | data == 100 | data == 1000) {						// базовые случаи
			if (data == 10) {
				out_data = "X";
			}
			if (data == 100) {
				out_data = "C";
			}
			if (data == 1000) {
				out_data = "M";
			}
		}
		
		else {
			String str = Integer.toString(data);								// перевели входное число в строку
			int rank_vol = str.length();										// чтобы понять сколько в нем разрядов
			for (int i = 0; i <= (str.length()) - 1; i++) {
				
				if (rank_vol == 3) {								// пока так реализовал, указателей не разбирал еще
					out_data += rank3.get(Integer.parseInt(String.valueOf(str.charAt(i))));
					rank_vol -= 1;
				}
				else if (rank_vol == 2) {
					out_data += rank2.get(Integer.parseInt(String.valueOf(str.charAt(i))));
					rank_vol -= 1;
				}
				else if (rank_vol == 1) {
					out_data += rank1.get(Integer.parseInt(String.valueOf(str.charAt(i))));
					rank_vol -= 1;
				}
				
			}
			
		}
		return out_data;
	}
	
}
