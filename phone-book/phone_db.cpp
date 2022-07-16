#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <map>
#include <cctype>
#include <utility>
#include <ctype.h>
#include <algorithm>
#include <string>

using std::vector;
using std::string;
using std::map;
using std::cout;
using std::cin;
using std::endl;
using std::tolower;
using std::sort;
using std::find;

typedef map<char, string> PhoneNumberCollection;

struct Name {
  string lastname;
  string firstname;
};

//Make name pair
Name make_name(const string &lastname, const string &firstname) {
  Name result = {lastname,firstname};
  return result;
}

//Make a string lowercase
string str_to_lower(const string &s) {
  int length = s.length()+1;
  string low_string(length,' ');
  for (int i=0;i<length-1;i++){
    low_string[i]=tolower(s[i]);
  }
  low_string[length-1]='\0';
  return low_string;
}

bool operator<(const Name &left,const Name &right) {
  //compare lastname first
  string left_lastname_lc = str_to_lower(left.lastname);
  string right_lastname_lc = str_to_lower(right.lastname);
  if (left_lastname_lc.compare(right_lastname_lc)<0) {
    return true;
  }
  if (right_lastname_lc.compare(left_lastname_lc)<0) {
    return false;
  }
  //if lastname no difference, compare first
  string left_firstname_lc = str_to_lower(left.firstname);
  string right_firstname_lc = str_to_lower(right.firstname);
  if (left_firstname_lc.compare(right_firstname_lc)<0) {
    return true;
  }
  if (right_firstname_lc.compare(left_firstname_lc)<0) {
    return false;
  }
  return false;//equal
}

bool operator==(const Name &left,const Name &right) {
  return !(left<right) && !(right<left);
}

int valid(string phone){
  char first = phone[0];
  char last = phone[phone.length()-1];
  if(!(isdigit(first) && isdigit(last))){
    return 0;
  }
  for (int i = 1; i < int(phone.length()-1) ;i++){
    if (!(phone[i]=='-' || isdigit(phone[i]))){
      return 0;
    }
  }
  return 1;//if valid, return 1
}

//Helper method for P command
int printAll(PhoneNumberCollection personCollect){
  int count=0;
  //print in alphabetical order by type
  if (personCollect.find('0') != personCollect.end()){
    cout<<"Result: "<< "CELL,"<<personCollect.at('0')<<endl;
    count++;
  }
  if (personCollect.find('1') != personCollect.end()){
    cout<<"Result: "<< "FAX,"<<personCollect.at('1')<<endl;
    count++;
  }
  if (personCollect.find('2') != personCollect.end()){
    cout<<"Result: "<< "HOME,"<<personCollect.at('2')<<endl;
    count++;
  }
  if (personCollect.find('3') != personCollect.end()){
    cout<<"Result: "<< "VOIP,"<<personCollect.at('3')<<endl;
    count++;
  }
  if (personCollect.find('4') != personCollect.end()){
    cout<<"Result: "<< "WORK,"<<personCollect.at('4')<<endl;
    count++;
  }
  if (count == 0){
    cout<<"Info: There are no phone numbers for this contact"<<endl;
    return 0;
  }
  cout<<"Info: Found "<<count<< " phone number(s) for this contact"<<endl;
  return 0;
}

//Map phone number type to char
char switchType(string type){
  if (type == "CELL"){
    return '0';
  } else if (type == "FAX"){
    return '1';
  } else if (type == "HOME"){
    return '2';
  } else if (type == "VOIP"){
    return '3';
  } else if (type == "WORK"){
    return '4';
  }
  return '5'; //not a valid type
}

//S command
int inputFile(string filename,vector<Name> name_db, map<Name, PhoneNumberCollection> phone_db){
  string lastname, firstname;
  Name make;
  PhoneNumberCollection personCollect;
  int size = name_db.size();
  std::ofstream ofile(filename);
  if (!ofile.is_open()) {
    cout << "Could not open output file" << endl;
    return 1;//error
  } else {
    ofile << size << endl;//how many people
    vector<Name>::iterator itr=name_db.begin();
    while (itr!=name_db.end()){
      lastname = (*itr).lastname;
      firstname = (*itr).firstname;
      ofile<<lastname<<" "<<firstname<<" ";//name
      make=make_name(lastname,firstname);
      if (phone_db.find(make)!=phone_db.end()){	
	personCollect=phone_db[make];
	int size = personCollect.size();
	ofile<<size<<" ";//how many phone number for each
	if (personCollect.find('0') != personCollect.end()){
	  ofile<< '0'<<" "<<personCollect.at('0')<<" ";
	}
	if (personCollect.find('1') != personCollect.end()){
	  ofile<< '1'<<" "<<personCollect.at('1')<<" ";
	}
	if (personCollect.find('2') != personCollect.end()){
	  ofile<< '2'<<" "<<personCollect.at('2')<<" ";
	}
	if (personCollect.find('3') != personCollect.end()){
	  ofile<< '3'<<" "<<personCollect.at('3')<<" ";
	}
	if (personCollect.find('4') != personCollect.end()){
	  ofile<< '4'<<" "<<personCollect.at('4')<<" ";
	}
      } else {
	ofile<<'0'<<" ";//this person have no number
      }
      itr++;
    }
  }
  return 0;
}

//R command
int loadFile(string filename, map<Name, PhoneNumberCollection> &phone_db, vector<Name> &name_db){
  int size, num;
  string number, lastname, firstname, line;
  char index;
  Name make;
  PhoneNumberCollection personPhone;
  std::ifstream ifile(filename);
  if (!ifile.is_open()) {
    return 1;
  } else {
    phone_db.clear();//replace old
    name_db.clear();
    ifile >> size;
    for (int i =0; i<size;i++){//each person
      ifile >> lastname >> firstname >> num;
      for (int j=0;j<num;j++){//each phone num
	ifile >> index >>number;
	personPhone[index]=number;
      }	
      make = make_name(lastname,firstname);
      name_db.push_back(make);
      if (num!=0){
	phone_db[make]=personPhone;
	personPhone.clear();
      }
    }
  }
  return 0;
}

void Ncommand(std::istream& cin, map<Name, PhoneNumberCollection> &phone_db, vector<Name> &name_db){
  char index;
  Name make;
  PhoneNumberCollection personPhone;
  string lastname, firstname, type, phone_number;
  cin >> lastname >> firstname >> type >> phone_number;
  make=make_name(lastname,firstname);
  if (std::find(name_db.begin(),name_db.end(),make)==name_db.end()){
    cout << "Error: Contact not found"<<endl;//check error conditions in order
  } else if (switchType(type)== '5'){
    cout << "Error: Invalid phone number type"<<endl;
  } else if (valid(phone_number) == 0){
    cout << "Error: Not a valid phone number"<<endl;
  } else {
    index = switchType(type);
    if (phone_db.find(make) != phone_db.end()){
      personPhone = phone_db[make];
      if (personPhone.find(index)!=personPhone.end()){//type is occupied
	personPhone[index]=phone_number;
	phone_db[make]=personPhone;
	cout << "Result: Phone number replaced"<<endl;
      } else {//available type
	personPhone[index]=phone_number;
	phone_db[make]=personPhone;
	cout << "Result: Phone number added"<<endl;
      }
    } else {//has no phone number before
      personPhone.clear();
      personPhone[index]=phone_number;
      phone_db[make] = personPhone;
      cout << "Result: Phone number added"<<endl;
    }
  }
}

void Xcommand(std::istream& cin, map<Name, PhoneNumberCollection> &phone_db, vector<Name> &name_db){
  Name make;
  PhoneNumberCollection personPhone;
  string lastname, firstname, type, phone_number;
  cin >> lastname >> firstname >> type;
  make=make_name(lastname,firstname);
  if (std::find(name_db.begin(),name_db.end(),make)==name_db.end()){
    cout << "Error: Contact not found"<<endl;
  } else {
    personPhone = phone_db.find(make)->second;
    if (personPhone.find(switchType(type))!=personPhone.end()){
      (phone_db.find(make)->second).erase(switchType(type));
      cout << "Result: Phone number deleted"<<endl;
    } else {
      cout << "Error: No phone number of that type"<<endl;
    }
  }
}

void Dcommand(std::istream& cin, map<Name, PhoneNumberCollection> &phone_db, vector<Name> &name_db){
  Name make;
  string lastname, firstname;
  cin >> lastname >> firstname;
  make=make_name(lastname,firstname);
  if (std::find(name_db.begin(),name_db.end(),make)==name_db.end()){
    cout << "Error: Contact not found"<<endl;
  } else {
    name_db.erase(remove(name_db.begin(),name_db.end(),make));
    cout << "Result: Contact deleted"<<endl;
  }
  if (phone_db.find(make) != phone_db.end()){
    phone_db.erase(make);
  }
}    

void Ccommand(std::istream& cin, vector<Name> &name_db){
  Name make;
  string lastname, firstname;
  cin >> lastname >> firstname;
  make=make_name(lastname,firstname);
  if (std::find(name_db.begin(),name_db.end(),make)!=name_db.end()){
    cout << "Error: Contact already exists"<<endl;
  }else{
    name_db.push_back(make);
    cout << "Result: Contact created"<<endl;
  }
}
  
int main() {
  map<Name, PhoneNumberCollection> phone_db;
  vector<Name> name_db;
  char command;
  Name make;
  string lastname, firstname, type, phone_number, filename, full;
  int errorCheck;
  vector<Name>::iterator its;
  map<string, PhoneNumberCollection>::iterator itr;
  PhoneNumberCollection personPhone, personCollect;
  cout<<"Info: Welcome to the Phone Database!"<<endl;
  cout<<"Info: Please enter a command"<<endl;
  while (cin >> command){//continue taking in input
    if(command== 'C'){//different operations based on command
      Ccommand(cin, name_db);
    } else if (command== 'D'){
      Dcommand(cin, phone_db, name_db);
    } else if (command== 'L'){
      sort(name_db.begin(),name_db.end());
      for(its = name_db.begin();its != name_db.end();++its) {
	cout << "Result: " <<(*its).lastname<<","<<(*its).firstname<<endl;
      }
      int size = name_db.size();
      cout << "Info: There are "<<size<<" contact(s)"<<endl;
    } else if (command== 'P'){
      cin >> lastname >> firstname;
      make=make_name(lastname,firstname);
      if (std::find(name_db.begin(),name_db.end(),make)==name_db.end()){
        cout << "Error: Contact not found"<<endl;
      } else if (phone_db.find(make) == phone_db.end()){
	cout<<"Info: There are no phone numbers for this contact"<<endl;
      } else {
	printAll(phone_db.find(make)->second);
      }
    } else if (command== 'N'){
      Ncommand(cin, phone_db, name_db);
    } else if (command== 'X'){
      Xcommand(cin, phone_db, name_db);
    } else if (command== 'S'){
      cin >> filename;	
      inputFile(filename, name_db, phone_db);
    } else if (command== 'R'){
      cin >> filename;
      errorCheck = loadFile(filename, phone_db, name_db);
      if (errorCheck==1) {
	cout << "Error: Could not open input file" << endl;
      }
    } else if (command== 'Q'){
      cout<<"Info: Thank you for using the Phone Database!"<<endl;
      return 0;
    } else {
      cout<<"Error: malformed input"<<endl;
    }
  cout<<"Info: Please enter a command"<<endl;
  }
  return 0;
}

