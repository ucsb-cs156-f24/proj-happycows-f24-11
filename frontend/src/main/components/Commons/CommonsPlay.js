import React, { useState} from "react";
import greetingsList from "../../../assets/PlayGreetings.json"
import "../../pages/HomePage.css"
import { Card } from "react-bootstrap";
export default function CommonsPlay({ currentUser }) {
  // Stryker disable  all 
  const firstName = currentUser?.root ? currentUser?.root?.user?.givenName : "";

  const [welcomeText, _]= useState(greetingsList[Math.floor(Math.random() * greetingsList.length)]);
  // Stryker restore all

  return (
    <div data-testid="CommonsPlay">
      <Card data-testid = "commons-card" className= "woodentitle">
      <div className= "text-center border-0 my-3">
        <h1 data-testid="commonsPlay-title" className="animate-charcter">{welcomeText} {firstName}!</h1>
      </div> 
    </Card>
    </div>
  );
};