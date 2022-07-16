//Name: Jessi(Jiaxin) Luo                                                       
//JHED ID: jluo30  

#include <cassert>
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <iterator>
#include <map>
#include "TTrie.h"
#include <cstring>

using namespace std;
using std::find;
using std::map;
using std::string;
using std::cout;
using std::endl;
using std::vector;


template<typename DataType>
TTrie<DataType>::TTrie(){
  endpoint = false;
}

// helper deallocation function
template<typename DataType>
void TTrie<DataType>::clear(){
  if (!children.empty()) {
    typename map<DataType, TTrie *>::iterator it;
    //delete each child
    for (it = children.begin(); it != children.end(); it++) {
      delete it->second;
    }
    children.clear();
  }
}

template<typename DataType>
TTrie<DataType>::~TTrie(){
  clear();
}

template<typename DataType>
TTrie<DataType>::TTrie(const TTrie& rhs){
  clear();
  typename map<DataType, TTrie *>::const_iterator its;
  for (its = rhs.children.begin(); its != rhs.children.end(); its++) {
    TTrie * insert = new TTrie(*(its->second));//copy each child
    insert->endpoint = (*its).second->endpoint;	
    DataType n = (*its).first;
    children.insert({n, insert});
  }
  endpoint = false;
}

template<typename DataType>
TTrie<DataType>& TTrie<DataType>::operator=(const TTrie<DataType> &rhs){
  if (this != &rhs){
     clear();	
     typename map<DataType, TTrie *>::const_iterator it;
     for (it = rhs.children.cbegin(); it != rhs.children.cend(); it++) {
     	 children[it->first] = new TTrie(*(it->second)); //recur
     }
     endpoint = rhs.endpoint;
  }
  return *this;
}

template<typename DataType>
TTrie<DataType>& TTrie<DataType>::operator+=(const vector<DataType>& sequence){
  //no more element
  if (sequence.size()==0) {
    endpoint = true;
    return *this;
  }
  //no node with this element
  if (children.empty() || children.find(*(sequence.begin())) == children.end()){
     TTrie * insert = new TTrie();
     children.insert({*(sequence.begin()), insert});
  }
  vector<DataType> add = sequence;
  add.erase(add.begin());
  *children[*(sequence.begin())]+=(add);
  return *this;
}

template<typename DataType>
bool TTrie<DataType>::operator^(const vector<DataType> &sequence) const{
  //all element exists
  if (sequence.size()==0) {
    return endpoint == true;
  }
  //if no match
  if (children.empty() || children.find(*(sequence.begin())) == children.end()){
     return false;
  }
  vector<DataType> add = sequence;
  add.erase(add.begin());
  typename map<DataType, TTrie *>::const_iterator it = children.find(*sequence.begin());
  return *(it->second)^(add);
}

template<typename DataType>
bool TTrie<DataType>::operator==(const TTrie<DataType>& rhs) const{
  //compare by size
  if (children.empty() && rhs.children.empty()){
    return true;
  }
  if (children.size()!=rhs.children.size()){
    return false;
  }
  typename map<DataType, TTrie *>::const_iterator it;
  for (it = children.cbegin(); it != children.cend(); it++) {
    DataType has = (*it).first;
    //compare endpoint and recusivelly compare child 
    if (rhs.children.find(has)== rhs.children.cend()){
      return false;
    }
    typename map<DataType, TTrie *>::const_iterator its = rhs.children.find(has);
    if (!(*(it->second)==*(its->second)) || (*(it->second)).endpoint!=(*(its->second)).endpoint){
      return false;
    }
  }
  return true;
}

//a recurssive helper method for operator <<
template<typename DataType>
void TTrie<DataType>::output_trie(std::ostream& os,const TTrie& n, vector<DataType> s) const {
  if (n.endpoint == true){ //reach end, output all elements in vector and connect with '-'
     os<< s.at(0);
     for(int i=1; i < s.size(); i++){
       os<<"-"<< s.at(i);
     }
     os<<endl;
  }
  typename map<DataType, TTrie *>::const_iterator it;
  for (it = n.children.cbegin(); it != n.children.cend(); it++) {
      vector<DataType> add = s;
      add.push_back(it->first);
      n.output_trie(os,*(it->second), add);//continue reading
  }
}

template<typename DataType>
std::ostream& operator<<(std::ostream& os, const TTrie<DataType>& tt){
  typename map<DataType, TTrie<DataType> *>::const_iterator it;
  for (it = tt.children.cbegin(); it != tt.children.cend(); it++) {
    vector<DataType> build;
    vector<DataType> add = build;
    add.push_back(it->first);
    tt.output_trie(os, *(it->second), add);
  }
  return os;
}

template<typename DataType>
unsigned TTrie<DataType>::numChildren() const {
  if (children.empty()) {
    return 0;
  }
  return children.size();
}

template<typename DataType>
bool TTrie<DataType>::hasChild() const {
  return !children.empty();
}

template<typename DataType>
bool TTrie<DataType>::hasChild(const DataType &value) const {
  if (children.empty()) {
    return false;
  }
  return children.find(value) != children.end();
}

template<typename DataType>
const TTrie<DataType>* TTrie<DataType>::getChild(const DataType &value) const{
  if (children.empty() || children.find(value) == children.end()) {
    return nullptr;
  }
  return children.at(value);
}

template<typename DataType>
bool TTrie<DataType>::isEndpoint() const {
  return endpoint;
}