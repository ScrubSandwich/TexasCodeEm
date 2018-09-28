import React, { Component } from 'react';
import "./Table.css"

class Table extends Component {
  render() {
    const viewBox = [0, 0, window.innerWidth, window.innerHeight];

    return (
      <div className="Table">
         <svg
            id="table"
            preserveAspectRatio="xMaxYMax none"
            viewBox={viewBox} >
            <ellipse cx="700" cy="360"
              rx="600" ry="300"
              stroke="#1aa70e" stroke-width="15"
              fill="#1aa70e"  />              
              <text x="530" y="275" font-size="50">Texas Code 'Em</text>
          </svg>
      </div>
    );
  }
}

export default Table;
