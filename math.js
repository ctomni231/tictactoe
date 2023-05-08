load('lib.js');

var function2 =function (number) {
    return increment(number);

}

var nextfunction = function(array) {
	var number = 0;
	for(var i = 0; i < array.length; i++){
		number += array[i];
	}
	return number;
}

var spacefunction = function(num1, num2) {
	return num1+num2;
}

var stringfunction = function() {
	return "hello world 55 22";
}