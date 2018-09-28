import React, { Component } from 'react';
import "./ButtonPanel.css"
import axios from 'axios'

class ButtonPanel extends Component {

  handleClick = () => {
    console.log("button clicked");
    const url = "http://localhost:8080/hello";

    axios.get(url)
      .then(res => {
        console.log("s");
        console.log(res);
      })
  }

  render() {
    return (
      <div className="ButtonPanel">
         <button onClick={this.handleClick}>Check</button>
         <button>Raise</button>
         <button>Fold</button>
      </div>
    );
  }
}

export default ButtonPanel;
