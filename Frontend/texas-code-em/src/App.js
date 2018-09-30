import React, { Component } from 'react';
import './App.css';
import Table from './Table';
import ButtonPanel from "./ButtonPanel"
import axios from "axios";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Table />
      </div>
    );
  }

  componentWillMount = () => {
    console.log("Getting userId from backend...");
    const url = "http://localhost:8080/api/generateUserId";

    axios.get(url)
      .then(res => {
        console.log("User ID: " + res.userId);

        localStorage.setItem("UserId", res.userId);
      })
  }
  
}

export default App;
