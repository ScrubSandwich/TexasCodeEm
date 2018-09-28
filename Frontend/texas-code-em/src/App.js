import React, { Component } from 'react';
import './App.css';
import Table from './Table';
import ButtonPanel from "./ButtonPanel"
import axios from 'axios'

class App extends Component {
  render() {
    return (
      <div className="App">
        <Table />
      </div>
    );
  }

  componentDidMount = () => {
    const url = "localhost:8080/hello";

    axios.get(url)
      .then(res => {
        console.log(res);
      })
  }
}

export default App;
