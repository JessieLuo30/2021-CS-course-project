//Name: Jessi(Jiaxin) Luo
//JHED ID: jluo30

#include <cassert>
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <iterator>
#include <map>
#include "CTrie.h"
#include <cstring>

using namespace std;
using std::find;
using std::map;
using std::string;
using std::cout;
using std::endl;

typedef map<char, CTrie *> childList;

CTrie::CTrie(){
  endpoint = false;
}

// helper deallocation function
void CTrie::clear(){
  if (!children.empty()) {
    childList::iterator it;
    //delete each child
    for (it = children.begin(); it != children.end(); it++) {
      delete it->second;
    }
    children.clear();
  }
}

CTrie::~CTrie(){
  clear();
}

CTrie::CTrie(const CTrie& rhs){                                               
  clear();                                                                
  childList::const_iterator its;                                               
  for (its = rhs.children.begin(); its != rhs.children.end(); its++) {    
    CTrie * insert = new CTrie(*(its->second));//copy each child
    insert->endpoint = (*its).second->endpoint;
    char n = (*its).first;
    children.insert({n, insert});                                             
  }                                                                           
  endpoint = false;                  
}

CTrie& CTrie::operator=(const CTrie &rhs){
  if (this != &rhs){
    clear();
    childList::const_iterator it;
    for (it = rhs.children.cbegin(); it != rhs.children.cend(); it++) {
      children[it->first] = new CTrie(*(it->second)); //recur
    }
    endpoint = rhs.endpoint;
  }
  return *this;
}

CTrie& CTrie::operator+=(const std::string& word){
  //no more char
  if (word.size()==0) {
    endpoint = true;
    return *this;
  }
  //no node with this char
  if (children.empty() || children.find(word.at(0)) == children.end()){
    CTrie * insert = new CTrie();
    children.insert({word.at(0), insert});
  }
  string add = word.substr(1);
  *children[word.at(0)]+=(add);
  return *this;
} 

bool CTrie::operator^(const std::string &word) const{
  //all char exists
  if (word.size()==0) {
    return endpoint==true;
  }
  //if no match
  if (children.empty() || children.find(word.at(0)) == children.end()){
     return false;
  }
  childList::const_iterator it = children.find(word.at(0));
  string add(word.begin() + 1, word.end());
  return *(it->second)^(add);
}

bool CTrie::operator==(const CTrie& rhs) const{
  //compare by size
  if (children.empty() && rhs.children.empty()){
    return true;
  }
  if (children.size()!=rhs.children.size()){
    return false;
  }
  childList::const_iterator it;
  for (it = children.cbegin(); it != children.cend(); it++) {
    char has = (*it).first;
    if (rhs.children.find(has)== rhs.children.cend()){
      return false;
    }
    childList::const_iterator its = rhs.children.find(has);
    //compare endpoint and recusivelly compare child 
    if (!(*(it->second)==*(its->second)) || (*(it->second)).endpoint!=(*(its->second)).endpoint){
      return false;
    }
  }
  return true;
} 

//a recurssive helper method for operator <<
void CTrie::output_trie(std::ostream& os,const CTrie& ct, string s) const {
  if (ct.endpoint == true){ //reach end, output full string
    os << s << endl;
  }
  childList::const_iterator it;
  for (it = ct.children.cbegin(); it != ct.children.cend(); it++) {
    ct.output_trie(os,*(it->second), s+(it->first));//continue reading
  }
}

std::ostream& operator<<(std::ostream& os, const CTrie& ct){
  childList::const_iterator it;
  for (it = ct.children.cbegin(); it != ct.children.cend(); it++) {
    string build = "";
    ct.output_trie(os, *(it->second), build+(it->first));
  }
  return os;
}

unsigned CTrie::numChildren() const {
  if (children.empty()) {
    return 0;
  }
  return children.size();
}

bool CTrie::hasChild() const {
  return !children.empty();
}

bool CTrie::hasChild(char character) const {
  if (children.empty()) {
    return false;
  }
  return children.find(character) != children.end();
}

const CTrie* CTrie::getChild(char character) const{
  if (children.empty() || children.find(character) == children.end()) {
    return nullptr;
  }
  return children.at(character);
}

bool CTrie::isEndpoint() const {
  return endpoint;
}
