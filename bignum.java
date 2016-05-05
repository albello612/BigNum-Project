package bignum;

/*“I pledge that this assignment has been completed in compliance with the 
Graduate Honor Code and that I have neither given nor received any unauthorized 
aid on this work. Further, I did not use any source codes from any other unauthorized 
sources, either modified or unmodified. The submitted programming assignment is 
solely done by me and is my original work.”*/


import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.*;
import java.util.Stack;
import java.io.*;
import java.util.Scanner;

 class bignum {
	 @SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		
		Scanner file = new Scanner(new File ("input.txt"));
		
		while (file.hasNext()){
		String[] arrayA = file.nextLine().split(" ");
		
		Stack printStack = new Stack();
		Stack useStack = new Stack();
		
		//print
		for (int i = 0; i < arrayA.length; i++){
			if (arrayA[i].startsWith("+")  || arrayA[i].startsWith("*") 
					|| arrayA[i].startsWith("^")){
				
				String operand2 = (String) printStack.peek();
				printStack.pop();
				String operand1;
				if (printStack.isEmpty()){
					operand1 = "";
				}
				else{
					operand1 = (String) printStack.peek();
					printStack.pop();
				}
				
				
				if (operand2 == ""){
					operand2 = "0";
				}
				
				if (operand1 == ""){
					operand1 = "0";
				}
				
				String operator = (String) arrayA[i];
				String next = "(" + operand1 + operator + operand2 + ")";
				
				printStack.push(next);
			}
			
			else {
				long number = Long.parseLong(arrayA[i]);
				List<Long> digits = new ArrayList<Long>();
				while (number > 0) {
					long digit = number % 10;
					number = number / 10;
					digits.add(digit);}
				
				String digs = "";
				
				for (int j = digits.size() - 1; j >= 0; j --){
					digs += digits.get(j);
					
				}
				printStack.push(digs);
			}
			
		}
		@SuppressWarnings("unchecked")
		String value = (String) printStack.peek();
		String value1 = value.substring(1, value.lastIndexOf(")"));
		
		
		List<Long> op1 = new ArrayList<Long>();
		List<Long> op2 = new ArrayList<Long>();
		List<Long> op3 = new ArrayList<Long>();
		
		for (int i = 0; i < arrayA.length; i++){
			if (arrayA[i].startsWith("+")){
				
				if (useStack.size() > 1){
				op1 = (List<Long>)useStack.peek();
				useStack.pop();
			
				op2 = (List<Long>)useStack.peek();
				useStack.pop();
				
				useStack.push(add(op1, op2));
				}
				else{
					i = arrayA.length;
				}
			}
			else if (arrayA[i].startsWith("*")){
				
				if (useStack.size() > 1){
				op1 = (List<Long>)useStack.peek();
				useStack.pop();
				
				op2 = (List<Long>)useStack.peek();
				useStack.pop();
				
				useStack.push(multiply(op1, op2));
				}
				else{
					i = arrayA.length;
				}
			}
			else if (arrayA[i].startsWith("^")){
				
				if (useStack.size() > 1){
				op1 = (List<Long>)useStack.peek();
				useStack.pop();
				
				op2 = (List<Long>)useStack.peek();
				op3 = (List<Long>)useStack.peek();
				useStack.pop();
				
				useStack.push(exponent(op1, op2, op3));
				}
				
				else{
					i = arrayA.length;
					useStack.pop();
				}
			}
			else {
				long number = Long.parseLong(arrayA[i]);
				List<Long> digits = new ArrayList<Long>();
				while (number > 0) {
					long digit = number % 10;
					number = number / 10;
					digits.add(digit);}
				useStack.push(digits);
			}
		}
		
		if (useStack.isEmpty()){
			System.out.print("Error. Insufficient Operands");
		}
		else {
			System.out.print(value1 + " = ");
			op1 = (List<Long>) useStack.peek();
			useStack.pop();
			
			if (useStack.isEmpty()){
				for (int j = op1.size() - 1; j >= 0; j --){
					System.out.print(op1.get(j));
			}
			}
				else{
					
					op2 = (List<Long>) useStack.peek();
					useStack.pop();
					String num1 = "";
					String num2 = "";
					
					for (int k = op1.size() - 1; k >= 0; k--){
						num1 += op1.get(k);
				}
					for (int i = op2.size() - 1; i >= 0; i --){
						num2 += op2.get(i);
				}
					System.out.print("Error. The numbers remaining in the stack are " + num1 + " and " + num2);
				}
				
				System.out.print("\n");
			}	
	}
		}
	 @SuppressWarnings("unchecked")
	//add
	public static List<Long> add(List<Long> op1, List<Long> op2){
		
		//if the first operand is bigger, fill in zeros in smaller operand
		if (op1.size() > op2.size()){
			int numberZ = op1.size() - op2.size();
			for (int z = 0; z < numberZ; z++){
				op2.add((long) 0);
			}
		}
		//if the second operand is bigger, fill in zeros in smaller operand
		else {
				int numberZ = op2.size() - op1.size();
				for (int z = 0; z < numberZ; z++){
					op1.add((long) 0);
			}
			}
		
		ArrayList<Long> summ = new ArrayList<Long>();
		for (int j = 0, carry = 0; j < op2.size(); j++){
			long first = op1.get((int) j);
			long second = op2.get((int) j);
			long digit = (first + second)  % 10;
				
			//attach sum of each digit to list "digits"
			summ.add((digit + carry) % 10);
			
			//set carry for the next digit
			carry = (int) ((first + second + carry) / 10);
					
			//if there is a leftover carry, attach to the end of the list
			if (j == op2.size() - 1 && carry > 0){
				summ.add((long) carry);
			}
	}
			return summ;
	}
	 @SuppressWarnings("unchecked")
	//multiply
	public static List<Long> multiply(List<Long> op1, List<Long> op2){
		
		ArrayList<Long> digits = new ArrayList<Long>();
		
		if (op1.isEmpty() || op2.isEmpty()){
			digits.add((long) 0);
		}
		else{
		//multiply each digit of op2 against first digit in op1
		for (int k = 0, carry = 0; k < op2.size(); k++){
			long multi2 = op2.get(k);
			long multi1 = op1.get(0);
			long digit = (((multi2 * multi1) + carry) % 10);
			
			digits.add(digit);
			//set carry for next digit
			carry = (int) (((multi2 * multi1) + carry)/ 10);
			
			if (k == op2.size() - 1 && carry > 0){
				digits.add((long) carry);
			}
		}
		
		//move through elements in op1
		for(int m = 1; m < op1.size(); m++){
			
			List<Long> partial = new ArrayList<Long>();
			for (int z = 0; z < m; z++){
				partial.add((long) 0);}
			
			for (long k = 0, carry = 0; k < op2.size(); k++){
				long multi2 = op2.get((int) k);
				long multi1 = op1.get((int) m);
				long digit = (((multi2 * multi1) + carry) % 10);
				
				partial.add(digit);
				
				carry = (((multi2 * multi1) + carry) / 10);
				
				if (k == op2.size() - 1 && carry > 0){
					partial.add(carry);
				}
			}
				
			int numberZ = partial.size() - digits.size();
			for (int z = 0; z < numberZ; z++){
				digits.add((long) 0);
				}
			
		for (int j = 0, carry = 0; j < partial.size(); j++){
			long first = partial.get(j);
			long second = digits.get(j);
			long digit = (first + second)  % 10;
			
			digits.set(j, (long) ((digit + carry) % 10));
			
			carry = (int) ((first + second + carry) / 10);
			
			if (j == partial.size() - 1 && carry > 0){
				digits.add((long) carry);
			}
		}
		}
		}
		return digits;
	}
	 @SuppressWarnings("unchecked")
	//exponent
	public static List<Long> exponent(List<Long> op1, List<Long> op2, List<Long> op3){
		ArrayList<Long> total = new ArrayList<Long>();
		
		if (op1.isEmpty()){
			total.add((long) 1);
		}
		
		else if (op1.get(0) == 1){
			total = (ArrayList<Long>) op2;
		}
		else {
			for(long q = op1.get(0); q > 1; q--){
				
				ArrayList<Long> partial = new ArrayList<Long>();
				
				partial = (ArrayList<Long>) multiply(op2, op3);
				
				op2 = partial;
				
				total = (ArrayList<Long>) op2;
				}
				
					}
		return total;

	}
 }
 

